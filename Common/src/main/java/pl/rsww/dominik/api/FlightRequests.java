package pl.rsww.dominik.api;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class FlightRequests {
    @Validated
    public record LocationRequest(
            @Nonnull String country,
            @Nonnull String city
    ) {
    }

    @Validated
    public record AgeRangePrice(
            @Nonnull Integer startingRange,
            @Nonnull Integer endingRange,
            @Nonnull BigDecimal price
    ){}

    public record CreateFlight(
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull LocationRequest departure,
            @Nonnull LocationRequest destination,
            @Nonnull LocalDate date
    ) {}
}
