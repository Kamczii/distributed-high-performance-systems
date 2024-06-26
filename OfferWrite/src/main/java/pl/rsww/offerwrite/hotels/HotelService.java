package pl.rsww.offerwrite.hotels;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.flights.FlightCommand;
import pl.rsww.tour_operator.api.HotelRequests;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final AggregateStore<Hotel, HotelEvent, UUID> hotelStore;

    public void handle(HotelCommand command) {
        if (command instanceof HotelCommand.ConfirmLock confirmLock) {
            handle(confirmLock);
        } else if (command instanceof HotelCommand.Lock lock) {
            handle(lock);
        } else if (command instanceof HotelCommand.ReleaseLock releaseLock) {
            handle(releaseLock);
        }
    }

    public void handle(HotelRequests.CreateHotel create) {
        try {
            hotelStore.getEntity(create.hotelId());
        } catch (EntityNotFoundException e) {
            hotelStore.add(Hotel.create(create));
        }
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

    public void handle(HotelCommand.ReleaseLock releaseLock) {
//        hotelStore.getAndUpdate(hotel -> hotel.confirmLock(hotelConfirmLockRequest.orderId()), hotelConfirmLockRequest.hotelId());
        //todo
    }

    public void handle(HotelRequests request) {
        if (request instanceof HotelRequests.CreateHotel createHotel) {
            handle(createHotel);
        }
    }
}
