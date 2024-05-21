package pl.rsww.offerwrite.api.response;

import jakarta.annotation.Nonnull;

import java.math.BigDecimal;
import java.util.UUID;

public sealed interface OfferResponse {
    record Lock(
            @Nonnull UUID offerId,
            @Nonnull UUID orderId,
            @Nonnull BigDecimal price,
            @Nonnull AvailableLockStatus status
    ) implements OfferResponse {}

    record ConfirmLock(
            @Nonnull UUID offerId,
            @Nonnull UUID orderId,
            @Nonnull AvailableLockStatus status
    ) implements OfferResponse {}
}
