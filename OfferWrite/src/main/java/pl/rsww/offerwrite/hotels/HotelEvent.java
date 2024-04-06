package pl.rsww.offerwrite.hotels;

import java.util.UUID;

public sealed interface HotelEvent {
    record Created(UUID uuid) implements HotelEvent {}
}
