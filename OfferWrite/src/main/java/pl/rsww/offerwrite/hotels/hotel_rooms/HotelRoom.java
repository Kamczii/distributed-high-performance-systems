package pl.rsww.offerwrite.hotels.hotel_rooms;

import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;

import java.util.Collection;

public record HotelRoom(String type, Integer capacity, Integer beds, Long count,
                        Collection<AgeRangePrice> priceList) {
    public HotelRoom {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity has to be a positive number");
    }
}
