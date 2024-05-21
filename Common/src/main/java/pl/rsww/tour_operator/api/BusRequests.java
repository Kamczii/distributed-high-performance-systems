package pl.rsww.tour_operator.api;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

public interface BusRequests {
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

    record CreateBus(
            @Nonnull String busNumber,
            @Nonnull Integer numberOfSeats,
            @Nonnull LocationRequest departure,
            @Nonnull LocationRequest destination,
            @Nonnull LocalDate date,
            @Nonnull Collection<AgeRangePrice> priceList
    ) implements BusRequests{}
}
