package pl.rsww.payment.api;

import jakarta.annotation.Nonnull;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location;
import pl.rsww.order.api.OrderEvent;

import java.math.BigDecimal;
import java.util.UUID;

public sealed interface PaymentEvent {
    record Pending(
            @Nonnull UUID paymentId,
            @Nonnull UUID orderId,
            @Nonnull Long userId,
            @Nonnull BigDecimal amount
    ) implements PaymentEvent {}

    record Success(
            @Nonnull UUID orderId,
            @Nonnull Long userId,
            @Nonnull Hotel hotel,
            @Nonnull Room room,
            @Nonnull Location location
    ) implements PaymentEvent {}

    record Fail(
            @Nonnull UUID orderId,
            @Nonnull Long userId
    ) implements PaymentEvent {}
}
