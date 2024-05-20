package pl.rsww.offerwrite.buses;

import lombok.Getter;
import org.springframework.data.util.Pair;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;
import pl.rsww.offerwrite.common.location.Location;
import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;
import pl.rsww.tour_operator.api.BusRequests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static pl.rsww.offerwrite.constants.Constants.LOCK_TIME_IN_SECONDS;

@Getter
public class Bus extends AbstractAggregate<BusEvent, String> {
;

    private Location departure;
    private Location destination;
    private String busNumber;
    private LocalDate date;
    private Integer capacity;
    private Set<Pair<UUID, Integer>> activeReservations;
    private Set<Pair<UUID, Integer>> activeConfirmations;
    private Collection<AgeRangePrice> priceList;

    Bus() {
    }

    @Override
    public String id() {
        return BusUtils.busId(busNumber, date);
    }

    Bus(
            Location departure,
            Location destination,
            String busNumber,
            LocalDate date,
            Integer capacity,
            Collection<AgeRangePrice> priceList
    ) {
        enqueue(new BusEvent.BusCreated(departure, destination, busNumber, date, capacity, priceList));
    }

    public static Bus create(BusRequests.CreateBus create) {
        var departure = new Location(create.departure().country(), create.departure().city());
        var destination = new Location(create.destination().country(), create.destination().city());
        var busNumber = create.busNumber();
        var date = create.date();
        var capacity = create.numberOfSeats();
        var priceList = create.priceList().stream()
                .map(price -> new AgeRangePrice(price.startingRange(), price.endingRange(), price.price()))
                .toList();
        return new Bus(departure, destination, busNumber, date, capacity, priceList);
    }

    @Override
    public void when(BusEvent event) {
        switch (event) {
            case BusEvent.BusCreated busCreated -> {
                id = BusUtils.busId(busCreated.busNumber(), busCreated.date());
                departure = busCreated.departure();
                destination = busCreated.destination();
                busNumber = busCreated.busNumber();
                date = busCreated.date();
                capacity = busCreated.capacity();
                activeReservations = new HashSet<>();
                activeConfirmations = new HashSet<>();
                priceList = busCreated.priceList();
            }
            case BusEvent.SeatReserved seatsReserved -> {
                if (seatsReserved.time().isAfter(LocalDateTime.now().minusSeconds(LOCK_TIME_IN_SECONDS))) {
                    capacity-=seatsReserved.seats();
                    activeReservations.add(Pair.of(seatsReserved.orderId(), seatsReserved.seats()));
                }
            }
            case BusEvent.SeatReleased released -> {
                findReservation(released.orderId(), released.seats()).ifPresent(reservation -> {
                    capacity+=released.seats();
                    activeReservations.remove(reservation);
                });
            }
            case BusEvent.SeatReservedConfirmed confirmed -> {
                capacity-=confirmed.seats();
                activeConfirmations.add(Pair.of(confirmed.orderId(), confirmed.seats()));
                findReservation(confirmed.orderId(), confirmed.seats())
                        .ifPresent(reservation -> {
                            capacity+=confirmed.seats();
                            activeReservations.remove(reservation);
                        });
            }
            case BusEvent.SeatConfirmationCanceled canceled -> {
                capacity+=canceled.seats();
                activeConfirmations.remove(Pair.of(canceled.orderId(), canceled.seats()));
            }

            default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }

    public static Bus empty() {
        return new Bus();
    }

    static String mapToStreamId(String id) {
        return "Bus-%s".formatted(id);
    }

    public void lock(Integer seats, UUID orderId) {
        if (!seatsAvailable(seats))
            throw new IllegalStateException("No available seats");

        enqueue(new BusEvent.SeatReserved(busNumber, date, seats, LocalDateTime.now(), orderId));
    }

    public void confirmLock(Integer seats, UUID orderId) {
        if (!reservationValid(orderId, seats))
            throw new IllegalStateException("Reservation not found");

        enqueue(new BusEvent.SeatReservedConfirmed(busNumber, date, seats, orderId));
    }

    public void releaseLock(Integer seats, UUID orderId) {
        if (!reservationValid(orderId, seats))
            throw new IllegalStateException("Reservation not found");

        enqueue(new BusEvent.SeatReleased(busNumber, date, seats, orderId));
    }

    public void cancelConfirmation(Integer seats, UUID orderId) {
        if(!confirmationValid(orderId, seats))
            throw new IllegalStateException("Confirmation not found");
        enqueue(new BusEvent.SeatConfirmationCanceled(busNumber, date, seats, orderId));
    }



    public BigDecimal getPrice(Integer age) {
        if (age < 18) {
            return BigDecimal.TWO;
        }
        return BigDecimal.TEN;
    }

    private boolean reservationValid(UUID orderId, Integer seats) {
        return this.activeReservations.stream()
                .anyMatch(pair -> pair.getFirst().equals(orderId) && pair.getSecond().equals(seats));
    }

    private boolean confirmationValid(UUID orderId, Integer seats) {
        return this.activeConfirmations.stream()
                .anyMatch(pair -> pair.getFirst().equals(orderId) && pair.getSecond().equals(seats));
    }

    private Optional<Pair<UUID, Integer>> findReservation(UUID orderId, Integer seats) {
        return this.activeReservations.stream()
                .filter(pair -> pair.getFirst().equals(orderId) && pair.getSecond().equals(seats))
                .findAny();
    }

    public boolean seatsAvailable(Integer seats) {
        return this.capacity >= seats;
    }

}
