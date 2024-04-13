package pl.rsww.offerwrite.hotels.hotel_rooms;

public record HotelRoom(String type, Integer capacity, Integer beds) {
    public HotelRoom {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity has to be a positive number");
    }
}
