package pl.rsww.tour_operator.api;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;


public sealed interface FlightRequests {
    @Validated
    record LocationRequest(
            @Nonnull String country,
            @Nonnull String city
    ) {
    }

    @Validated
    record AgeRangePrice(
            @Nonnull Integer startingRange,
            @Nonnull Integer endingRange,
            @Nonnull BigDecimal price
    ){}

    record CreateFlight(
            @Nonnull String flightNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull LocationRequest departure,
            @Nonnull LocationRequest destination,
            @Nonnull LocalDate date,
            @Nonnull Collection<FlightRequests.AgeRangePrice> priceList
    ) implements FlightRequests{}
}
