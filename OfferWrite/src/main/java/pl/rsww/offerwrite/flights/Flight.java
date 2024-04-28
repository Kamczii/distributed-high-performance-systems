package pl.rsww.offerwrite.flights;

import lombok.Getter;
import org.springframework.data.util.Pair;
import pl.rsww.dominik.api.FlightRequests;
import pl.rsww.offerwrite.common.location.Location;
import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Flight extends AbstractAggregate<FlightEvent, String> {
    public static final int LOCK_TIME_MINUTES = 1;
    private Location departure;
    private Location destination;
    private String flightNumber;
    private LocalDate date;
    private Integer capacity;
    private Set<UUID> activeReservations;

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
            }
            case FlightEvent.SeatReserved seatsReserved -> {
                if (seatsReserved.time().isAfter(LocalDateTime.now().minusMinutes(LOCK_TIME_MINUTES))) {
                    capacity-=seatsReserved.seats();
                    activeReservations.add(seatsReserved.reservationId());
                }
            }
            case FlightEvent.SeatReservedConfirmed confirmed -> {
                    capacity-=confirmed.seats();
                    activeReservations.remove(confirmed.reservationId());
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

    public void reserveSeats(Integer seats, UUID reservationId) {
        if (!seatsAvailable(seats))
            throw new IllegalStateException("No available seats");

        enqueue(new FlightEvent.SeatReserved(seats, LocalDateTime.now(), reservationId));
    }

    public void confirmReservation(Integer seats, UUID reservationId) {
        if (!reservationValid(reservationId))
            throw new IllegalStateException("Reservation not found");

        enqueue(new FlightEvent.SeatReservedConfirmed(seats, reservationId));
    }

    private boolean reservationValid(UUID reservationId) {
        return this.activeReservations.contains(reservationId);
    }

    private boolean seatsAvailable(Integer seats) {
        return this.capacity >= seats;
    }


}
