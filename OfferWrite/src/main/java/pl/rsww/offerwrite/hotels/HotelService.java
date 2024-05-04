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

    public Hotel getEntity(UUID id) {
        return hotelStore.getEntity(id);
    }

    public void handle(HotelCommand.Lock hotelLockRequest) {
        hotelStore.getAndUpdate(
                hotel -> hotel.lock(hotelLockRequest.orderId(), hotelLockRequest.type(), hotelLockRequest.startDate(), hotelLockRequest.endDate()),
                hotelLockRequest.hotelId());
    }

    public void handle(HotelCommand.ConfirmLock hotelConfirmLockRequest) {
        hotelStore.getAndUpdate(hotel -> hotel.confirmLock(hotelConfirmLockRequest.orderId()), hotelConfirmLockRequest.hotelId());
    }
}
