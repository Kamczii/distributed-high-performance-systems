package pl.rsww.offerwrite.flights;

import pl.rsww.offerwrite.common.location.Location;

import java.time.LocalDate;

public sealed interface FlightEvent {
    record FlightCreated(Location departure,
                         Location destination,
                         String flightNumber,
                         LocalDate date,
                         Integer capacity) implements FlightEvent {}
}
