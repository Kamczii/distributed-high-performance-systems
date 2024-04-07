package pl.rsww.offerwrite.api.requests;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Collection;

public final class FlightRequests {
    @Validated
    public record LocationRequest(
            @Nonnull String country,
            @Nonnull String city
    ) {
    }

    public record CreateFlight(
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull LocationRequest departure,
            @Nonnull LocationRequest destination,
            @Nonnull Collection<LocalDate> departures
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
