package pl.rsww.tour_operator.api;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface UpdatePriceListRequests {
    @Validated
    record AgeRangePrice(
            @Nonnull Integer startingRange,
            @Nonnull Integer endingRange,
            @Nonnull BigDecimal price
    ){}

    record UpdateBusPrices(
            @Nonnull String busNumber,
            @Nonnull Collection<AgeRangePrice> priceList
    ) implements UpdatePriceListRequests{}

    record UpdateFlightPrices(
            @Nonnull String flightNumber,
            @Nonnull Collection<AgeRangePrice> priceList
    ) implements UpdatePriceListRequests{}

    record UpdateHotelRoomPrices(
            @Nonnull UUID hotelId,
            @Nonnull String type,
            @Nonnull Collection<AgeRangePrice> priceList
    ) implements UpdatePriceListRequests{}
}
