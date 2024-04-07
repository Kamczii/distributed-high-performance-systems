package pl.rsww.offerwrite.hotels;

import pl.rsww.offerwrite.api.requests.HotelRequests;
import pl.rsww.offerwrite.common.location.Location;
import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRooms;

import java.util.UUID;
import java.util.stream.Collectors;

import static pl.rsww.offerwrite.hotels.HotelEvent.HotelCreated;


public class Hotel extends AbstractAggregate<HotelEvent, UUID> {
    private UUID hotelId;
    private HotelRooms rooms;
    private Location location;

    Hotel() {
    }

    public static Hotel empty() {
        return new Hotel();
    }

    Hotel(
            UUID hotelId,
            Location location,
            HotelRooms rooms
    ) {
        enqueue(new HotelCreated(hotelId, location, rooms));
    }

    public static Hotel create(HotelRequests.CreateHotel create) {
        var location = new Location(create.location().country(), create.location().city());
        var rooms = create.rooms()
                .stream()
                .map(roomRequest -> new HotelRoom(roomRequest.type(), roomRequest.beds()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), HotelRooms::new));
        return new Hotel(create.hotelId(), location, rooms);
    }

    @Override
    public void when(HotelEvent event) {
        switch (event) {
            case HotelCreated hotelCreated -> {
                hotelId = hotelCreated.hotelId();
                location = hotelCreated.location();
                rooms = hotelCreated.rooms();
            }
        }
    }

    static String mapToStreamId(UUID hotelId) {
        return "Hotel-%s".formatted(hotelId);
    }
}
