package pl.rsww.dominik.api;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public final class HotelRequests {

    @Validated
    public record AgeRangePrice(
            @Nonnull Integer startingRange,
            @Nonnull Integer endingRange,
            @Nonnull BigDecimal price
            ){}

    @Validated
    public record RoomRequest(
            @Nonnull String type,
            @Nonnull Integer capacity,
            @Nonnull Integer beds,
            @Nonnull Collection<AgeRangePrice> priceList
    ) {
    }

    @Validated
    public record LocationRequest(
            @Nonnull String country,
            @Nonnull String city
    ) {
    }

    public record CreateHotel(
            @Nonnull UUID hotelId,
            @Nonnull String name,
            @Nonnull LocationRequest location,
            @Nonnull Collection<RoomRequest> rooms
    ) {}
}
