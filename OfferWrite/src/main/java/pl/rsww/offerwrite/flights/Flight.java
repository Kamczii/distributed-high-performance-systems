package pl.rsww.offerwrite.flights;

import lombok.Getter;
import org.springframework.data.util.Pair;
import pl.rsww.dominik.api.FlightRequests;
import pl.rsww.offerwrite.common.location.Location;
import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Getter
public class Flight extends AbstractAggregate<FlightEvent, String> {
    public static final int LOCK_TIME_IN_SECONDS = 2;
    private Location departure;
    private Location destination;
    private String flightNumber;
    private LocalDate date;
    private Integer capacity;
    private Set<Pair<UUID, Integer>> activeReservations;
    private Set<Pair<UUID, Integer>> activeConfirmations;

    Flight() {
    }

    @Override
    public String id() {
        return FlightUtils.flightId(flightNumber, date);
    }

    Flight(
            Location departure,
            Location destination,
            String flightNumber,
            LocalDate date,
            Integer capacity
    ) {
        enqueue(new FlightEvent.FlightCreated(departure, destination, flightNumber, date, capacity));
    }

    public static Flight create(FlightRequests.CreateFlight create) {
        var departure = new Location(create.departure().country(), create.departure().city());
        var destination = new Location(create.destination().country(), create.destination().city());
        var flightNumber = create.flightNumber();
        var date = create.date();
        var capacity = create.numberOfSeats();

        return new Flight(departure, destination, flightNumber, date, capacity);
    }

    @Override
    public void when(FlightEvent event) {
        switch (event) {
            case FlightEvent.FlightCreated flightCreated -> {
                id = FlightUtils.flightId(flightCreated.flightNumber(), flightCreated.date());
                departure = flightCreated.departure();
                destination = flightCreated.destination();
                flightNumber = flightCreated.flightNumber();
                date = flightCreated.date();
                capacity = flightCreated.capacity();
                activeReservations = new HashSet<>();
                activeConfirmations = new HashSet<>();
            }
            case FlightEvent.SeatReserved seatsReserved -> {
                if (seatsReserved.time().isAfter(LocalDateTime.now().minusSeconds(LOCK_TIME_IN_SECONDS))) {
                    capacity-=seatsReserved.seats();
                    activeReservations.add(Pair.of(seatsReserved.orderId(), seatsReserved.seats()));
                }
            }
            case FlightEvent.SeatReleased released -> {
                findReservation(released.orderId(), released.seats()).ifPresent(reservation -> {
                    capacity+=released.seats();
                    activeReservations.remove(reservation);
                });
            }
            case FlightEvent.SeatReservedConfirmed confirmed -> {
                capacity-=confirmed.seats();
                activeConfirmations.add(Pair.of(confirmed.orderId(), confirmed.seats()));
                findReservation(confirmed.orderId(), confirmed.seats())
                        .ifPresent(reservation -> {
                            capacity+=confirmed.seats();
                            activeReservations.remove(reservation);
                        });
            }
            case FlightEvent.SeatConfirmationCanceled canceled -> {
                capacity+=canceled.seats();
                activeConfirmations.remove(Pair.of(canceled.orderId(), canceled.seats()));
            }

            default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }

    public static Flight empty() {
        return new Flight();
    }

    static String mapToStreamId(String id) {
        return "Flight-%s".formatted(id);
    }

    public void lock(Integer seats, UUID orderId) {
        if (!seatsAvailable(seats))
            throw new IllegalStateException("No available seats");

        enqueue(new FlightEvent.SeatReserved(flightNumber, date, seats, LocalDateTime.now(), orderId));
    }

    public void confirmLock(Integer seats, UUID orderId) {
        if (!reservationValid(orderId, seats))
            throw new IllegalStateException("Reservation not found");

        enqueue(new FlightEvent.SeatReservedConfirmed(flightNumber, date, seats, orderId));
    }

    public void releaseLock(Integer seats, UUID orderId) {
        if (!reservationValid(orderId, seats))
            throw new IllegalStateException("Reservation not found");

        enqueue(new FlightEvent.SeatReleased(flightNumber, date, seats, orderId));
    }

    public void cancelConfirmation(Integer seats, UUID orderId) {
        if(!confirmationValid(orderId, seats))
            throw new IllegalStateException("Confirmation not found");
        enqueue(new FlightEvent.SeatConfirmationCanceled(flightNumber, date, seats, orderId));
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

    private boolean seatsAvailable(Integer seats) {
        return this.capacity >= seats;
    }
}
