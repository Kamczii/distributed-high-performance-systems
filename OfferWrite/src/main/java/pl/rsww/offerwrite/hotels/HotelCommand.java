package pl.rsww.offerwrite.hotels;

import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.UUID;

public sealed interface HotelCommand {

    record Lock(
            @Nonnull UUID hotelId,
            @Nonnull UUID orderId,
            @Nonnull String type,
            @Nonnull LocalDate startDate,
            @Nonnull LocalDate endDate
    ) implements HotelCommand{}


    record ConfirmLock(
            @Nonnull UUID hotelId,
            @Nonnull UUID orderId
    ) implements HotelCommand{}
}
