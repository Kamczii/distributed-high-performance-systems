package pl.rsww.payment.api;

import jakarta.annotation.Nonnull;

import java.math.BigDecimal;
import java.util.UUID;

public sealed interface PaymentEvent {
    record Success(
            @Nonnull UUID orderId,
            @Nonnull Long userId
    ) implements PaymentEvent {}

    record Fail(
            @Nonnull UUID orderId,
            @Nonnull Long userId
    ) implements PaymentEvent {}
}
