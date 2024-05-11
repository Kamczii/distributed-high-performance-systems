package pl.rsww.order.api;

import jakarta.annotation.Nonnull;

import java.math.BigDecimal;
import java.util.UUID;

public sealed interface OrderEvent {
    record Pending(
            @Nonnull UUID orderId,
            @Nonnull Long userId,
            @Nonnull BigDecimal price
    ) implements OrderEvent {}

    record Cancelled(
            @Nonnull UUID orderId
    ) implements OrderEvent {}
}
