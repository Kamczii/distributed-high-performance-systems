package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.rsww.offerwrite.api.integration.AvailableOfferStatus;
import pl.rsww.offerwrite.core.events.EventEnvelope;
import pl.rsww.offerwrite.flights.Flight;
import pl.rsww.offerwrite.flights.FlightEvent;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.flights.FlightUtils;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightRepository;
import pl.rsww.offerwrite.hotels.Hotel;
import pl.rsww.offerwrite.hotels.HotelEvent;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoomRepository;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.mapper.AgeRangePriceMapper;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferEvent;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;
import pl.rsww.offerwrite.producer.ObjectRequestKafkaProducer;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_INTEGRATION_TOPIC;
import static pl.rsww.offerwrite.constants.Constants.LOCK_TIME_IN_SECONDS;

@Component
@RequiredArgsConstructor
public class OfferIntegrationEvent {
    private static final int WAIT = 10;
    private final FlightService flightService;
    private final HotelService hotelService;
    private final ObjectRequestKafkaProducer objectRequestKafkaProducer;
    private final FlightRepository flightRepository;
    private final OfferRepository offerRepository;
    private final PriceCalculatorService priceCalculatorService;
    private final AgeRangePriceMapper ageRangePriceMapper;
    private final HotelRoomRepository hotelRoomRepository;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final OfferStatusService offerStatusService;

    @EventListener
    void handleSeatReserved(EventEnvelope<FlightEvent.SeatReserved> eventEnvelope) {
        final var flightNumber = eventEnvelope.data().flightNumber();
        final var flightDate = eventEnvelope.data().date();
        final var flightCurrentState = getFlightCurrentState(flightNumber, flightDate);
        final var availableSeats = flightCurrentState.getCapacity();
        final var flight = fetchFlightEntity(flightNumber, flightDate);
        findAffectedOffers(availableSeats, flight)
                .stream()
                .map(offer -> map(offer, AvailableOfferStatus.RESERVED))
                .forEach(this::publish);
    }

    private Flight getFlightCurrentState(String flightNumber, LocalDate flightDate) {
        return flightService.get(FlightUtils.flightId(flightNumber, flightDate));
    }

    private pl.rsww.offerwrite.flights.getting_flight_seats.Flight fetchFlightEntity(String flightNumber, LocalDate flightDate) {
        return flightRepository.findByFlightNumberAndDate(flightNumber, flightDate);
    }

    private List<Offer> findAffectedOffers(Integer availableSeats, pl.rsww.offerwrite.flights.getting_flight_seats.Flight flight) {
        return offerRepository.findAllByHotelRoomCapacityGreaterThanAndInitialFlightIdOrReturnFlightId(availableSeats, flight.getId(), flight.getId());
    }

    private pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.StatusChanged map(Offer offer, AvailableOfferStatus status) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.StatusChanged(offer.getId(), status);
    }

    private void publish(pl.rsww.offerwrite.api.integration.OfferIntegrationEvent event) {

        objectRequestKafkaProducer.produce(OFFER_INTEGRATION_TOPIC, event);

        if (event instanceof pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.StatusChanged statusChanged &&
                statusChanged.status().equals(AvailableOfferStatus.RESERVED)) {
            scheduleStatusUpdate(statusChanged);
        }
    }

    private void scheduleStatusUpdate(pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.StatusChanged statusChanged) {
        executorService.schedule(() -> {
            offerStatusService.updateStatus(statusChanged.offerId());
        }, LOCK_TIME_IN_SECONDS + WAIT, TimeUnit.SECONDS);
    }

    @EventListener
    void handleSeatReleased(EventEnvelope<FlightEvent.SeatReleased> eventEnvelope) {
        final var state = getFlightCurrentState(eventEnvelope.data().flightNumber(), eventEnvelope.data().date());
        final var flight = fetchFlightEntity(eventEnvelope.data().flightNumber(), eventEnvelope.data().date());
        final var availableSeats = state.getCapacity();
        findAffectedOffers(availableSeats, flight)
                .stream()
                .filter(this::isOfferAvailable)
                .map(offer -> map(offer, AvailableOfferStatus.OPEN))
                .forEach(this::publish);
    }

    private boolean isOfferAvailable(Offer offer) {
        final var hotelId = offer.getHotelRoom().getHotel().getId();
        final var roomType = offer.getHotelRoom().getType();
        final var hotelCurrentState = getHotelCurrentState(hotelId);
        final var availableSeats = Math.min(getFlightCurrentState(offer.getInitialFlight().getFlightId()).getCapacity(),
                getFlightCurrentState(offer.getReturnFlight().getFlightId()).getCapacity());
        return hotelCurrentState.getRooms().rooms().stream().anyMatch(room ->
                roomType.equals(room.type()) && availableSeats >= room.capacity()
        );
    }

    private Hotel getHotelCurrentState(UUID hotelId) {
        return hotelService.getEntity(hotelId);
    }

    private Flight getFlightCurrentState(String id) {
        return flightService.get(id);
    }

    @EventListener
    void handleSeatConfirmationCanceled(EventEnvelope<FlightEvent.SeatConfirmationCanceled> eventEnvelope) {
        final var flightNumber = eventEnvelope.data().flightNumber();
        final var flightDate = eventEnvelope.data().date();
        final var state = getFlightCurrentState(flightNumber, flightDate);
        final var flight = fetchFlightEntity(flightNumber, flightDate);
        final var availableSeats = state.getCapacity();
        findAffectedOffers(availableSeats, flight)
                .stream()
                .filter(this::isOfferAvailable)
                .map(offer -> map(offer, AvailableOfferStatus.OPEN))
                .forEach(this::publish);
    }

    @EventListener
    void handleSeatReservedConfirmed(EventEnvelope<FlightEvent.SeatReservedConfirmed> eventEnvelope) {
        final var state = getFlightCurrentState(eventEnvelope.data().flightNumber(), eventEnvelope.data().date());
        final var flight = fetchFlightEntity(eventEnvelope.data().flightNumber(), eventEnvelope.data().date());
        final var flightCapacity = state.getCapacity();
        findAffectedOffers(flightCapacity, flight)
                .stream()
                .map(offer -> map(offer, AvailableOfferStatus.ENDED))
                .forEach(this::publish);
    }

    @EventListener
    void handleRoomReserved(EventEnvelope<HotelEvent.RoomReserved> eventEnvelope) {
        final var event = eventEnvelope.data();
        final var hotelCurrentState = getHotelCurrentState(event.hotelId());
        final var shouldChangeStatus = !hotelCurrentState.roomAvailable(event.type(), event.checkInDate(), event.checkOutDate());
        if (shouldChangeStatus) {
            findAllOffersWithRoom(event.type(), event.hotelId())
                               .stream()
                               .map(offer -> map(offer, AvailableOfferStatus.RESERVED))
                               .forEach(this::publish);
        }
    }

    private List<Offer> findAllOffersWithRoom(String type, UUID hotelId) {
        return hotelRoomRepository.findAllByTypeAndHotelId(type, hotelId)
                                  .stream()
                                  .map(HotelRoom::getId)
                                  .collect(Collectors.collectingAndThen(Collectors.toList(), this::findAllWithRoom));
    }

    private List<Offer> findAllWithRoom(Collection<UUID> hotelRoomIds) {
        return offerRepository.findAllByHotelRoomIdIn(hotelRoomIds);
    }

    @EventListener
    void handleRoomConfirmed(EventEnvelope<HotelEvent.RoomConfirmed> eventEnvelope) {
        final var event = eventEnvelope.data();
        final var hotelCurrentState = getHotelCurrentState(event.hotelId());
        final var shouldChangeStatus = !hotelCurrentState.roomAvailable(event.type(), event.checkInDate(), event.checkOutDate());
        if (shouldChangeStatus) {
            findAllOffersWithRoom(event.type(), event.hotelId())
                    .stream()
                    .map(offer -> map(offer, AvailableOfferStatus.RESERVED))
                    .forEach(this::publish);
        }
    }

    @EventListener
    public void onEvent(pl.rsww.offerwrite.offer.getting_offers.OfferEvent event) {
        onOfferCreated(event);
    }

    private void onOfferCreated(OfferEvent offerEvent) {
        var offer = offerRepository.findById(offerEvent.getEntityId())
                .orElseThrow();

        var departure = mapLocation(offer.getInitialFlight().getDeparture());
        var destination = mapLocation(offer.getInitialFlight().getDestination());
        var room = mapRoom(offer);
        var hotel = mapHotel(offer, room);
        var start = offer.getInitialFlight().getDate();
        var end = offer.getReturnFlight().getDate();
        var priceList = getPriceListForOffer(offer);
        var event = new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Created(offer.getId(), hotel, departure, destination, start, end, AvailableOfferStatus.OPEN, priceList);
        publish(event);
    }

    private static pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location mapLocation(Location offer) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location(offer.getCity(), offer.getCountry());
    }

    private static pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room mapRoom(Offer offer) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room(offer.getHotelRoom().getType(), offer.getHotelRoom().getCapacity(), offer.getHotelRoom().getBeds());
    }

    private static pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel mapHotel(Offer offer, pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room room) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel(offer.getHotelRoom().getHotel().getName(), room);
    }

    private Collection<pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.AgeRangePrice> getPriceListForOffer(Offer offer) {
        final var priceList = priceCalculatorService.getPriceList(offer.getId());
        return ageRangePriceMapper.map(priceList);
    }

    @TransactionalEventListener
    public void onTransactionalEvent(pl.rsww.offerwrite.offer.getting_offers.OfferEvent event) {
        onOfferCreated(event);
    }
}
