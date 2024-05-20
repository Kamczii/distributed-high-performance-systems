package pl.rsww.offerwrite.buses;

import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.UUID;

public sealed interface BusCommand {
    record Lock(
            @Nonnull String busNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull UUID orderId,
            @Nonnull LocalDate date
    ) implements BusCommand {}

    record ReleaseLock(
            @Nonnull String busNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull UUID orderId,
            @Nonnull LocalDate date
    ) implements BusCommand {}

    record ConfirmLock(
            @Nonnull String busNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull UUID orderId,
            @Nonnull LocalDate date
    ) implements BusCommand {}

    record CancelConfirmation(
            @Nonnull String busNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull UUID orderId,
            @Nonnull LocalDate date
    ) implements BusCommand {}
}
