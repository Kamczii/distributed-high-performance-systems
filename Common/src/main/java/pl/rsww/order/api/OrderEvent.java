package pl.rsww.order.api;

import jakarta.annotation.Nonnull;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location;

import java.math.BigDecimal;
import java.util.UUID;

public sealed interface OrderEvent {
    record Pending(
            @Nonnull UUID orderId,
            @Nonnull Long userId,
            @Nonnull BigDecimal price,
            @Nonnull Hotel hotel,
            @Nonnull Room room,
            @Nonnull Location location
    ) implements OrderEvent {}

    record Cancelled(
            @Nonnull UUID orderId
    ) implements OrderEvent {}
}
