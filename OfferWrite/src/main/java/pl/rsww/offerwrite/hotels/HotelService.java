package pl.rsww.offerwrite.hotels;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final AggregateStore<Hotel, HotelEvent, UUID> hotelStore;

    void test() {

    }
}
