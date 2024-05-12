package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.rsww.offerwrite.api.integration.AvailableOfferStatus;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;
import pl.rsww.offerwrite.core.events.EventEnvelope;
import pl.rsww.offerwrite.flights.Flight;
import pl.rsww.offerwrite.flights.FlightEvent;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.flights.FlightUtils;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightRepository;
import pl.rsww.offerwrite.hotels.Hotel;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.mapper.AgeRangePriceMapper;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferEvent;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;
import pl.rsww.offerwrite.producer.ObjectRequestKafkaProducer;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_INTEGRATION_TOPIC;

@Component
@RequiredArgsConstructor
public class OfferIntegrationEvent {
    private final FlightService flightService;
    private final HotelService hotelService;
    private final ObjectRequestKafkaProducer objectRequestKafkaProducer;
    private final FlightRepository flightRepository;
    private final OfferRepository offerRepository;
    private final PriceCalculatorService priceCalculatorService;
    private final AgeRangePriceMapper ageRangePriceMapper;
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
    public void onEvent(pl.rsww.offerwrite.offer.getting_offers.OfferEvent event) {
        onOfferCreated(event);
    }

    @TransactionalEventListener
    public void onTransactionalEvent(pl.rsww.offerwrite.offer.getting_offers.OfferEvent event) {
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

    private Collection<pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.AgeRangePrice> getPriceListForOffer(Offer offer) {
        final var priceList = priceCalculatorService.getPriceList(offer.getId());
        return ageRangePriceMapper.map(priceList);
    }

    private static pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel mapHotel(Offer offer, pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room room) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel(offer.getHotelRoom().getHotel().getName(), room);
    }

    private static pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location mapLocation(Location offer) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location(offer.getCity(), offer.getCountry());
    }

    private static pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room mapRoom(Offer offer) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room(offer.getHotelRoom().getType(), offer.getHotelRoom().getCapacity(), offer.getHotelRoom().getBeds());
    }

    private Hotel getHotelCurrentState(UUID hotelId) {
        return hotelService.getEntity(hotelId);
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

    private List<Offer> findAffectedOffers(Integer availableSeats, pl.rsww.offerwrite.flights.getting_flight_seats.Flight flight) {
        return offerRepository.findAllByHotelRoomCapacityGreaterThanAndInitialFlightIdOrReturnFlightId(availableSeats, flight.getId(), flight.getId());
    }

    private pl.rsww.offerwrite.flights.getting_flight_seats.Flight fetchFlightEntity(String flightNumber, LocalDate flightDate) {
        return flightRepository.findByFlightNumberAndDate(flightNumber, flightDate);
    }

    private Flight getFlightCurrentState(String flightNumber, LocalDate flightDate) {
        return flightService.get(FlightUtils.flightId(flightNumber, flightDate));
    }

    private Flight getFlightCurrentState(String id) {
        return flightService.get(id);
    }

    private pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.StatusChanged map(Offer offer, AvailableOfferStatus status) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.StatusChanged(offer.getId(), status);
    }
    private void publish(pl.rsww.offerwrite.api.integration.OfferIntegrationEvent event) {
        objectRequestKafkaProducer.produce(OFFER_INTEGRATION_TOPIC, event);
    }
}
