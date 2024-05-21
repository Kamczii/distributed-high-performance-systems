package pl.rsww.payment.api;

import jakarta.annotation.Nonnull;
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
            @Nonnull Long userId
    ) implements PaymentEvent {}

    record Fail(
            @Nonnull UUID orderId,
            @Nonnull Long userId
    ) implements PaymentEvent {}
}
