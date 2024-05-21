package pl.rsww.offerwrite.common.age_range_price;

import jakarta.annotation.Nonnull;

import java.math.BigDecimal;

public record AgeRangePrice(
        @Nonnull Integer startingRange,
        @Nonnull Integer endingRange,
        @Nonnull BigDecimal price
){}
