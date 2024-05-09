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

    record SeatReserved(String flightNumber, LocalDate date, Integer seats, LocalDateTime time, UUID orderId) implements FlightEvent {}
    record SeatReleased(String flightNumber, LocalDate date, Integer seats, UUID orderId) implements FlightEvent {}
    record SeatReservedConfirmed(String flightNumber, LocalDate date, Integer seats, UUID orderId) implements FlightEvent {}
    record SeatConfirmationCanceled(String flightNumber, LocalDate date, Integer seats, UUID orderId) implements FlightEvent {}
}
