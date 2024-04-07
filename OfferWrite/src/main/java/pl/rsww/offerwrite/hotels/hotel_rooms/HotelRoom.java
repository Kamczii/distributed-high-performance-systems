package pl.rsww.offerwrite.hotels.hotel_rooms;

public record HotelRoom(String type, Integer beds) {
    public HotelRoom {
        if (beds <= 0)
            throw new IllegalArgumentException("Beds has to be a positive number");
    }
}
