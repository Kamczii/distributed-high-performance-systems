package pl.rsww.offerwrite.offer.getting_offers.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.integration.AvailableTransportType;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePriceHelper;
import pl.rsww.offerwrite.flights.Flight;
import pl.rsww.offerwrite.flights.FlightCommand;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.hotels.Hotel;
import pl.rsww.offerwrite.hotels.HotelCommand;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.offer.getting_offers.FlightOffer;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.strategy.OfferStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlightOfferOfferStrategyImpl implements OfferStrategy<FlightOffer> {

    private final FlightService flightService;

    private final HotelService hotelService;

    @Override
    public Location getInitialLocation(Offer offer) {
        final var flightOffer = cast(offer);
        return flightOffer.getInitialFlight().getDeparture();
    }

    @Override
    public Location getDestinationLocation(Offer offer) {
        final var flightOffer = cast(offer);
        return flightOffer.getInitialFlight().getDestination();
    }

    @Override
    public LocalDate getStartDate(Offer offer) {
        final var flightOffer = cast(offer);
        return flightOffer.getInitialFlight().getDate();
    }

    @Override
    public LocalDate getEndDate(Offer offer) {
        final var flightOffer = cast(offer);
        return flightOffer.getReturnFlight().getDate();
    }

    @Override
    public int getAvailableSeats(Offer offer) {
        final var flightOffer = cast(offer);
        return Math.min(getFlightCurrentState(flightOffer.getInitialFlight().getFlightId()).getCapacity(),
                getFlightCurrentState(flightOffer.getReturnFlight().getFlightId()).getCapacity());
    }

    @Override
    public FlightOffer cast(Offer offer) {
        return (FlightOffer) offer;
    }

    @Override
    public void lock(Offer offer, UUID orderId, int visitorsCount) {

        final var flightOffer = cast(offer);
        ArrayList<FlightCommand> rollbacks = new ArrayList<>();
        try {
            final var initialFlightRequest = buildFlightLockRequest(orderId, flightOffer.getInitialFlight(), visitorsCount);
            flightService.handle(initialFlightRequest);
            rollbacks.add(buildFlightLockReleaseRequest(orderId, flightOffer.getInitialFlight(), visitorsCount));

            final var returnFlightRequest = buildFlightLockRequest(orderId, flightOffer.getReturnFlight(), visitorsCount);
            flightService.handle(returnFlightRequest);
            rollbacks.add(buildFlightLockReleaseRequest(orderId, flightOffer.getReturnFlight(), visitorsCount));

            final var hotelLockRequest = buildHotelLockRequest(orderId, flightOffer.getHotelRoom(), flightOffer.getInitialFlight().getDate(), flightOffer.getReturnFlight().getDate());
            hotelService.handle(hotelLockRequest);
            log.info(String.format("Successful lock (Offer %s, Order %s)", offer.getId(), orderId));
        } catch (Exception e) {
            rollbacks.forEach(this::handle);
            throw new IllegalStateException(String.format("Failed to lock (Offer %s, Order %s), reason: %s", offer.getId(), orderId, e.getMessage()));
        }
    }

    @Override
    public void confirmLock(Offer offer, UUID orderId) {
        final var flightOffer = cast(offer);
        final var capacity = offer.getHotelRoom().getCapacity(); //todo moze nie dzialac potwuierdzeie zakupu o mniejszej liczbie uczestinkow niz przewiduje oferta
        ArrayList<FlightCommand> flightRollbacks = new ArrayList<>();
        try {
            final var initialFlightRequest = buildFlightConfirmLockRequest(orderId, flightOffer.getInitialFlight(), capacity);
            flightService.handle(initialFlightRequest);
            flightRollbacks.add(buildFlightCancelConfirmationRequest(orderId, flightOffer.getInitialFlight(), capacity));

            final var returnFlightRequest = buildFlightConfirmLockRequest(orderId, flightOffer.getReturnFlight(), capacity);
            flightService.handle(returnFlightRequest);
            flightRollbacks.add(buildFlightCancelConfirmationRequest(orderId, flightOffer.getReturnFlight(), capacity));

            final var hotelConfirmLockRequest = buildHotelConfirmLockRequest(orderId, offer);
            hotelService.handle(hotelConfirmLockRequest);
        } catch (Exception e) {
            flightRollbacks.forEach(this::handle);
            throw new IllegalStateException("Failed to confirm offer");
        }
    }

    @Override
    public boolean isOfferAvailable(Offer offer) {
        final var flightOffer = cast(offer);
        final var initialFlight = getFlightCurrentState(flightOffer.getInitialFlight().getFlightId());
        final var returnFlight = getFlightCurrentState(flightOffer.getReturnFlight().getFlightId());
        final var hotelRoom = offer.getHotelRoom();
        final var hotel = getHotelCurrentState(hotelRoom.getHotel().getId());

        return Stream.of(
                hotel.roomAvailable(hotelRoom.getType(), flightOffer.getInitialFlight().getDate(), flightOffer.getReturnFlight().getDate()),
                initialFlight.seatsAvailable(hotelRoom.getCapacity()),
                returnFlight.seatsAvailable(hotelRoom.getCapacity())
        ).allMatch(TRUE::equals);
    }

    private static FlightCommand.Lock buildFlightLockRequest(UUID orderId, pl.rsww.offerwrite.flights.getting_flight_seats.Flight flight, Integer capacity) {
        return new FlightCommand.Lock(flight.getFlightNumber(), capacity, orderId, flight.getDate());
    }

    private static FlightCommand.ReleaseLock buildFlightLockReleaseRequest(UUID orderId, pl.rsww.offerwrite.flights.getting_flight_seats.Flight flight, Integer capacity) {
        return new FlightCommand.ReleaseLock(flight.getFlightNumber(), capacity, orderId, flight.getDate());
    }

    private static FlightCommand.CancelConfirmation buildFlightCancelConfirmationRequest(UUID orderId, pl.rsww.offerwrite.flights.getting_flight_seats.Flight flight, Integer capacity) {
        return new FlightCommand.CancelConfirmation(flight.getFlightNumber(), capacity, orderId, flight.getDate());
    }

    private HotelCommand.ConfirmLock buildHotelConfirmLockRequest(UUID orderId, Offer offer) {
        return new HotelCommand.ConfirmLock(offer.getHotelRoom().getHotel().getId(), orderId);
    }

    private static HotelCommand.Lock buildHotelLockRequest(UUID orderId, HotelRoom room, LocalDate start, LocalDate end) {
        return new HotelCommand.Lock(room.getHotel().getId(),
                orderId,
                room.getType(),
                start,
                end);
    }

    private void handle(FlightCommand command) {
        try {
            flightService.handle(command);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void handle(HotelCommand command) {
        try {
            hotelService.handle(command);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static FlightCommand.ConfirmLock buildFlightConfirmLockRequest(UUID orderId, pl.rsww.offerwrite.flights.getting_flight_seats.Flight flight, Integer capacity) {
        return new FlightCommand.ConfirmLock(flight.getFlightNumber(), capacity, orderId, flight.getDate());
    }

    private Flight getFlightCurrentState(String id) {
        return flightService.get(id);
    }

    private Hotel getHotelCurrentState(UUID hotelId) {
        return hotelService.getEntity(hotelId);
    }

    @Override
    public BigDecimal calculatePrice(Offer offer, Collection<Integer> ageOfVisitors) {
        final var flightOffer = cast(offer);
        final var tripDuration = getTripDuration(flightOffer);

        final var hotel = getHotel(offer);
        final var component1 = calculateTotalPrice(ageOfVisitors, age -> hotel.getPrice(offer.getHotelRoom().getType(), age))
                .multiply(BigDecimal.valueOf(tripDuration));

        final var initialFlight = getFlight(flightOffer.getInitialFlight());
        final var component2 = calculateTotalPrice(ageOfVisitors, initialFlight::getPrice);

        final var returnFlight = getFlight(flightOffer.getReturnFlight());
        final var component3 = calculateTotalPrice(ageOfVisitors, returnFlight::getPrice);

        return component1.add(component2).add(component3);
    }

    @Override
    public Collection<AgeRangePrice> getPriceList(Offer offer) {
        final var flightOffer = cast(offer);
        final var hotel = getHotel(flightOffer);
        final var room = hotel.getRooms().rooms().stream()
                              .filter(r -> r.type().equals(offer.getHotelRoom().getType()))
                              .findAny()
                              .orElseThrow();
        final var initialFlight = getFlight(flightOffer.getInitialFlight());
        final var returnFlight = getFlight(flightOffer.getReturnFlight());

        final var priceLists = Stream.of(room.priceList(), initialFlight.getPriceList(), returnFlight.getPriceList()).toList();
        return AgeRangePriceHelper.calculateSummedPrices(priceLists);
    }

    @Override
    public AvailableTransportType getTransport(Offer offer) {
        return AvailableTransportType.FLIGHT;
    }

    private BigDecimal calculateTotalPrice(Collection<Integer> ageOfVisitors, Function<Integer, BigDecimal> priceFunction) {
        return ageOfVisitors.stream()
                            .map(priceFunction)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Flight getFlight(pl.rsww.offerwrite.flights.getting_flight_seats.Flight flight) {
        final var initialFlightId = flight.getFlightId();
        return flightService.get(initialFlightId);
    }

    private Hotel getHotel(Offer offer) {
        final var hotelId = offer.getHotelRoom().getHotel().getId();
        return hotelService.getEntity(hotelId);
    }

    private static long getTripDuration(FlightOffer offer) {
        return ChronoUnit.DAYS.between(offer.getInitialFlight().getDate(), offer.getReturnFlight().getDate());
    }
}
