package pl.rsww.dominik.api;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public final class HotelRequests {

    @Validated
    public record RoomRequest(
            @Nonnull String type,
            @Nonnull Integer capacity,
            @Nonnull Integer beds
    ) {
    }

    @Validated
    public record LocationRequest(
            @Nonnull String country,
            @Nonnull String city
    ) {
    }

    public record CreateHotel(
            //TODO: Dominik
            @Nonnull UUID hotelId,
            @Nonnull String name,
            @Nonnull LocationRequest location,
            @Nonnull Collection<RoomRequest> rooms
    ) {}

    public record RoomReserved(
            @Nonnull UUID hotelId,
            @Nonnull String type,
            @Nonnull LocalDate startDate,
            @Nonnull LocalDate endDate
    ) {}

    public record RoomBooked(
            @Nonnull UUID hotelId,
            @Nonnull String type,
            @Nonnull LocalDate startDate,
            @Nonnull LocalDate endDate
    ) {}
}
