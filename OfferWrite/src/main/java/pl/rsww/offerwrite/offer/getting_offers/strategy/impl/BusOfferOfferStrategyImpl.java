package pl.rsww.offerwrite.offer.getting_offers.strategy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.integration.AvailableTransportType;
import pl.rsww.offerwrite.buses.Bus;
import pl.rsww.offerwrite.buses.BusCommand;
import pl.rsww.offerwrite.buses.BusService;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePriceHelper;
import pl.rsww.offerwrite.hotels.Hotel;
import pl.rsww.offerwrite.hotels.HotelCommand;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.offer.getting_offers.BusOffer;
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
public class BusOfferOfferStrategyImpl implements OfferStrategy<BusOffer> {

    private final BusService busService;
    private final HotelService hotelService;

    @Override
    public Location getInitialLocation(Offer offer) {
        final var busOffer = cast(offer);
        return busOffer.getInitialbus().getDeparture();
    }

    @Override
    public Location getDestinationLocation(Offer offer) {
        final var busOffer = cast(offer);
        return busOffer.getInitialbus().getDestination();
    }

    @Override
    public LocalDate getStartDate(Offer offer) {
        final var busOffer = cast(offer);
        return busOffer.getInitialbus().getDate();
    }

    @Override
    public LocalDate getEndDate(Offer offer) {
        final var busOffer = cast(offer);
        return busOffer.getReturnbus().getDate();
    }

    @Override
    public int getAvailableSeats(Offer offer) {
        final var busOffer = cast(offer);
        return Math.min(getBusCurrentState(busOffer.getInitialbus().getBusId()).getCapacity(),
                getBusCurrentState(busOffer.getReturnbus().getBusId()).getCapacity());
    }

    private Bus getBusCurrentState(String id) {
        return busService.get(id);
    }

    @Override
    public BusOffer cast(Offer offer) {
        return (BusOffer) offer;
    }

    @Override
    public void lock(Offer offer, UUID orderId, int visitorsCount) {
        final var busOffer = cast(offer);
        ArrayList<BusCommand> rollbacks = new ArrayList<>();
        try {
            final var initialBusRequest = buildBusLockRequest(orderId, busOffer.getInitialbus(), visitorsCount);
            busService.handle(initialBusRequest);
            rollbacks.add(buildBusLockReleaseRequest(orderId, busOffer.getInitialbus(), visitorsCount));

            final var returnBusRequest = buildBusLockRequest(orderId, busOffer.getReturnbus(), visitorsCount);
            busService.handle(returnBusRequest);
            rollbacks.add(buildBusLockReleaseRequest(orderId, busOffer.getReturnbus(), visitorsCount));

            final var hotelLockRequest = buildHotelLockRequest(orderId, busOffer.getHotelRoom(), busOffer.getInitialbus().getDate(), busOffer.getReturnbus().getDate());
            hotelService.handle(hotelLockRequest);
            log.info(String.format("Successful lock (Offer %s, Order %s)", offer.getId(), orderId));
        } catch (Exception e) {
            rollbacks.forEach(this::handle);
            throw new IllegalStateException(String.format("Failed to lock (Offer %s, Order %s), reason: %s", offer.getId(), orderId, e.getMessage()));
        }
    }

    @Override
    public void confirmLock(Offer offer, UUID orderId) {
        final var busOffer = cast(offer);
        final var capacity = offer.getHotelRoom().getCapacity(); //todo moze nie dzialac potwuierdzeie zakupu o mniejszej liczbie uczestinkow niz przewiduje oferta
        ArrayList<BusCommand> busRollbacks = new ArrayList<>();
        try {
            final var initialBusRequest = buildBusConfirmLockRequest(orderId, busOffer.getInitialbus(), capacity);
            busService.handle(initialBusRequest);
            busRollbacks.add(buildBusCancelConfirmationRequest(orderId, busOffer.getInitialbus(), capacity));

            final var returnBusRequest = buildBusConfirmLockRequest(orderId, busOffer.getReturnbus(), capacity);
            busService.handle(returnBusRequest);
            busRollbacks.add(buildBusCancelConfirmationRequest(orderId, busOffer.getReturnbus(), capacity));

            final var hotelConfirmLockRequest = buildHotelConfirmLockRequest(orderId, offer);
            hotelService.handle(hotelConfirmLockRequest);
        } catch (Exception e) {
            busRollbacks.forEach(this::handle);
            throw new IllegalStateException("Failed to confirm offer");
        }
    }

    private static BusCommand.Lock buildBusLockRequest(UUID orderId, pl.rsww.offerwrite.buses.getting_bus_seats.Bus bus, Integer capacity) {
        return new BusCommand.Lock(bus.getBusNumber(), capacity, orderId, bus.getDate());
    }

    private static BusCommand.ReleaseLock buildBusLockReleaseRequest(UUID orderId, pl.rsww.offerwrite.buses.getting_bus_seats.Bus bus, Integer capacity) {
        return new BusCommand.ReleaseLock(bus.getBusNumber(), capacity, orderId, bus.getDate());
    }

    private static BusCommand.CancelConfirmation buildBusCancelConfirmationRequest(UUID orderId, pl.rsww.offerwrite.buses.getting_bus_seats.Bus bus, Integer capacity) {
        return new BusCommand.CancelConfirmation(bus.getBusNumber(), capacity, orderId, bus.getDate());
    }

    private HotelCommand.ConfirmLock buildHotelConfirmLockRequest(UUID orderId, Offer offer) {
        return new HotelCommand.ConfirmLock(offer.getHotelRoom().getHotel().getId(), orderId);
    }
    
    private static BusCommand.ConfirmLock buildBusConfirmLockRequest(UUID orderId, pl.rsww.offerwrite.buses.getting_bus_seats.Bus bus, Integer capacity) {
        return new BusCommand.ConfirmLock(bus.getBusNumber(), capacity, orderId, bus.getDate());
    }
    

    private static HotelCommand.Lock buildHotelLockRequest(UUID orderId, HotelRoom room, LocalDate start, LocalDate end) {
        return new HotelCommand.Lock(room.getHotel().getId(),
                orderId,
                room.getType(),
                start,
                end);
    }

    @Override
    public boolean isOfferAvailable(Offer offer) {
        final var busOffer = cast(offer);
        final var initialbus = getBusCurrentState(busOffer.getInitialbus().getBusId());
        final var returnbus = getBusCurrentState(busOffer.getReturnbus().getBusId());
        final var hotelRoom = offer.getHotelRoom();
        final var hotel = getHotel(offer);

        return Stream.of(
                hotel.roomAvailable(hotelRoom.getType(), busOffer.getInitialbus().getDate(), busOffer.getReturnbus().getDate()),
                initialbus.seatsAvailable(hotelRoom.getCapacity()),
                returnbus.seatsAvailable(hotelRoom.getCapacity())
        ).allMatch(TRUE::equals);
    }

    @Override
    public BigDecimal calculatePrice(Offer offer, Collection<Integer> ageOfVisitors) {
        final var busOffer = cast(offer);
        final var tripDuration = getTripDuration(busOffer);

        final var hotel = getHotel(offer);
        final var component1 = calculateTotalPrice(ageOfVisitors, age -> hotel.getPrice(offer.getHotelRoom().getType(), age))
                .multiply(BigDecimal.valueOf(tripDuration));

        final var initialFlight = getBusCurrentState(busOffer.getInitialbus().getBusId());
        final var component2 = calculateTotalPrice(ageOfVisitors, initialFlight::getPrice);

        final var returnFlight = getBusCurrentState(busOffer.getReturnbus().getBusId());
        final var component3 = calculateTotalPrice(ageOfVisitors, returnFlight::getPrice);

        return component1.add(component2).add(component3);
    }

    private BigDecimal calculateTotalPrice(Collection<Integer> ageOfVisitors, Function<Integer, BigDecimal> priceFunction) {
        return ageOfVisitors.stream()
                            .map(priceFunction)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static long getTripDuration(BusOffer offer) {
        return ChronoUnit.DAYS.between(offer.getInitialbus().getDate(), offer.getReturnbus().getDate());
    }

    @Override
    public Collection<AgeRangePrice> getPriceList(Offer offer) {
        final var busOffer = cast(offer);
        final var hotel = getHotel(busOffer);
        final var room = hotel.getRooms().rooms().stream()
                              .filter(r -> r.type().equals(offer.getHotelRoom().getType()))
                              .findAny()
                              .orElseThrow();
        final var initialBus = getBusCurrentState(busOffer.getInitialbus().getBusId());
        final var returnBus = getBusCurrentState(busOffer.getReturnbus().getBusId());

        final var priceLists = Stream.of(room.priceList(), initialBus.getPriceList(), returnBus.getPriceList()).toList();
        return AgeRangePriceHelper.calculateSummedPrices(priceLists);
    }

    private Hotel getHotel(Offer offer) {
        final var hotelId = offer.getHotelRoom().getHotel().getId();
        return hotelService.getEntity(hotelId);
    }

    @Override
    public AvailableTransportType getTransport(Offer offer) {
        return AvailableTransportType.BUS;
    }

    private void handle(BusCommand command) {
        try {
            busService.handle(command);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
