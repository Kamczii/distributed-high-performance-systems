package pl.rsww.offerwrite.flights;

import pl.rsww.offerwrite.common.location.Location;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public sealed interface FlightEvent {
    record FlightCreated(Location departure,
                         Location destination,
                         String flightNumber,
                         LocalDate date,
                         Integer capacity) implements FlightEvent {}

    record SeatReserved(Integer seats, LocalDateTime time, UUID reservationId) implements FlightEvent {}
    record SeatReservedConfirmed(Integer seats, UUID reservationId) implements FlightEvent {}
}
