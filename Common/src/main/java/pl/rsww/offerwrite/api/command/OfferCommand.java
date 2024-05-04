package pl.rsww.offerwrite.api.command;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public sealed interface OfferCommand {
    record Lock(
            @Nonnull UUID offerId,
            @Nonnull UUID orderId
    ) implements OfferCommand {}

    record ConfirmLock(
            @Nonnull UUID offerId,
            @Nonnull UUID orderId
    ) implements OfferCommand {}
}