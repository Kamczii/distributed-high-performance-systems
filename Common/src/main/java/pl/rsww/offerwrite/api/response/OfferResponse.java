package pl.rsww.offerwrite.api.response;

import jakarta.annotation.Nonnull;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location;

import java.math.BigDecimal;
import java.util.UUID;

public sealed interface OfferResponse {
    record Lock(
            @Nonnull UUID offerId,
            @Nonnull UUID orderId,
            @Nonnull BigDecimal price,
            @Nonnull AvailableLockStatus status,
            @Nonnull Hotel hotel,
            @Nonnull Location location
    ) implements OfferResponse {}

    record ConfirmLock(
            @Nonnull UUID offerId,
            @Nonnull UUID orderId,
            @Nonnull AvailableLockStatus status
    ) implements OfferResponse {}
}
