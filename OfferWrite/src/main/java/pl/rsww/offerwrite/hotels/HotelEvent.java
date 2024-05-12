package pl.rsww.offerwrite.hotels;

import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;
import pl.rsww.offerwrite.common.location.Location;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRooms;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public sealed interface HotelEvent {
    record HotelCreated(UUID hotelId, String name, Location location, HotelRooms rooms) implements HotelEvent {}
    record RoomReserved(UUID orderId, String type, LocalDateTime time, LocalDate checkInDate, LocalDate checkOutDate) implements HotelEvent {}
    record RoomConfirmed(UUID orderId, String type, LocalDate checkInDate, LocalDate checkOutDate) implements HotelEvent {}
}
