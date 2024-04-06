package pl.rsww.offerwrite.hotels;

import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;

import java.util.UUID;

public class Hotel extends AbstractAggregate<HotelEvent, UUID> {

    @Override
    public void when(HotelEvent hotelEvent) {

    }

    public static Hotel empty() {
        return new Hotel();
    }

    static String mapToStreamId(UUID shoppingCartId) {
        return "Hotel-%s".formatted(shoppingCartId);
    }
}
