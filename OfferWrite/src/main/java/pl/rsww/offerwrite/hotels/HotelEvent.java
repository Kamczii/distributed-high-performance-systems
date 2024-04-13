package pl.rsww.offerwrite.hotels;

import pl.rsww.offerwrite.common.location.Location;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRooms;

import java.util.UUID;

public sealed interface HotelEvent {
    record HotelCreated(UUID hotelId, String name, Location location, HotelRooms rooms) implements HotelEvent {}
}
