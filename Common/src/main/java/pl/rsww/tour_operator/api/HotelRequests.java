package pl.rsww.tour_operator.api;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

public sealed interface HotelRequests {

    @Validated
    record AgeRangePrice(
            @Nonnull Integer startingRange,
            @Nonnull Integer endingRange,
            @Nonnull BigDecimal price
            ){}

    @Validated
    record RoomRequest(
            @Nonnull String type,
            @Nonnull Integer capacity,
            @Nonnull Integer beds,
            @Nonnull Collection<AgeRangePrice> priceList
    ) {
    }

    @Validated
    record LocationRequest(
            @Nonnull String country,
            @Nonnull String city
    ) {
    }

    record CreateHotel(
            @Nonnull UUID hotelId,
            @Nonnull String name,
            @Nonnull LocationRequest location,
            @Nonnull Collection<RoomRequest> rooms
    ) implements HotelRequests {}
}
