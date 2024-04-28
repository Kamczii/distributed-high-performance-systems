package pl.rsww.dominik.api;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.UUID;

public final class FlightRequests {
    @Validated
    public record LocationRequest(
            @Nonnull String country,
            @Nonnull String city
    ) {
    }

    public record CreateFlight(
            //TODO: Dominik
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull LocationRequest departure,
            @Nonnull LocationRequest destination,
            @Nonnull LocalDate date
    ) {}

    public record SeatReserved(
            // TODO: Dima
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull UUID orderId,
            @Nonnull LocalDate date
    ) {}

    public record SeatConfirmed(
            // TODO: Dima
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull UUID orderId,
            @Nonnull LocalDate date
    ) {}
}
