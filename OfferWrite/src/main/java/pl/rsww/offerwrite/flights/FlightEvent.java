package pl.rsww.offerwrite.flights;

import java.util.UUID;

public sealed interface FlightEvent {
    record FlightCreated(UUID uuid) implements FlightEvent {}
}
