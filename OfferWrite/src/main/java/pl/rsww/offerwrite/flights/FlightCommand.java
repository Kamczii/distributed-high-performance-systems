package pl.rsww.offerwrite.flights;

import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public sealed interface FlightCommand {
    record Lock(
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull UUID orderId,
            @Nonnull LocalDate date
    ) implements FlightCommand {}

    record ReleaseLock(
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull UUID orderId,
            @Nonnull LocalDate date
    ) implements FlightCommand {}

    record ConfirmLock(
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull UUID orderId,
            @Nonnull LocalDate date
    ) implements FlightCommand {}

    record CancelConfirmation(
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull UUID orderId,
            @Nonnull LocalDate date
    ) implements FlightCommand {}
}
