package pl.rsww.preference.api;

import jakarta.annotation.Nonnull;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;

public sealed interface PreferenceEvent {
    record Destination(
            @Nonnull String country,
            @Nonnull String city
    ) implements PreferenceEvent {}

    record Hotel(
            @Nonnull String name,
            @Nonnull OfferIntegrationEvent.Room room
    ) implements PreferenceEvent {}
}
