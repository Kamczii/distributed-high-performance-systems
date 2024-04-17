package pl.rsww.dominik.api;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

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
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull LocalDate date
    ) {}

    public record SeatBooked(
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull LocalDate date
    ) {}
}
