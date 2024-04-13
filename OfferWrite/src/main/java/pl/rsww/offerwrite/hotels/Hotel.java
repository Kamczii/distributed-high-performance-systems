package pl.rsww.offerwrite.hotels;

import lombok.Getter;
import pl.rsww.offerwrite.api.requests.HotelRequests;
import pl.rsww.offerwrite.common.location.Location;
import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRooms;

import java.util.UUID;
import java.util.stream.Collectors;

import static pl.rsww.offerwrite.hotels.HotelEvent.HotelCreated;

@Getter
public class Hotel extends AbstractAggregate<HotelEvent, UUID> {
    private HotelRooms rooms;
    private Location location;
    private String name;

    Hotel() {
    }

    public static Hotel empty() {
        return new Hotel();
    }

    Hotel(
            UUID hotelId,
            String name,
            Location location,
            HotelRooms rooms
    ) {
        enqueue(new HotelCreated(hotelId, name, location, rooms));
    }

    public static Hotel create(HotelRequests.CreateHotel create) {
        var location = new Location(create.location().country(), create.location().city());
        var rooms = create.rooms()
                .stream()
                .map(roomRequest -> new HotelRoom(roomRequest.type(), roomRequest.beds()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), HotelRooms::new));
        return new Hotel(create.hotelId(), create.name(), location, rooms);
    }

    @Override
    public void when(HotelEvent event) {
        switch (event) {
            case HotelCreated hotelCreated -> {
                id = hotelCreated.hotelId();
                location = hotelCreated.location();
                name = hotelCreated.name();
                rooms = hotelCreated.rooms();
            }
        }
    }

    static String mapToStreamId(UUID hotelId) {
        return "Hotel-%s".formatted(hotelId);
    }
}
