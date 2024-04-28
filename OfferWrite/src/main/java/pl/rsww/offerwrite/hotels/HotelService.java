package pl.rsww.offerwrite.hotels;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rsww.dominik.api.HotelRequests;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final AggregateStore<Hotel, HotelEvent, UUID> hotelStore;

    public void create(HotelRequests.CreateHotel create) {
        hotelStore.add(Hotel.create(create));
    }
}
