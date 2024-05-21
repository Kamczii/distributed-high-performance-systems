package pl.rsww.offerwrite.buses;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;
import pl.rsww.tour_operator.api.BusRequests;

@Service
@RequiredArgsConstructor
public class BusService {

    private final AggregateStore<Bus, BusEvent, String> busStore;

    public void handle(BusRequests.CreateBus create) {
        try {
            Bus exist = busStore.getEntity(BusUtils.busId(create.busNumber(), create.date()));
        } catch (EntityNotFoundException e) {
            busStore.add(Bus.create(create));
        }
    }

    public void handle(BusCommand.ConfirmLock create) {
        final var busId = BusUtils.busId(create.busNumber(), create.date());
        busStore.getAndUpdate(
                current -> current.confirmLock(create.numberOfSeats(), create.orderId()),
                busId
        );
    }

    public void handle(BusCommand.Lock create) {
        final var busId = BusUtils.busId(create.busNumber(), create.date());
        busStore.getAndUpdate(
                current -> current.lock(create.numberOfSeats(), create.orderId()),
                busId
        );
    }

    public void handle(BusCommand.ReleaseLock releaseLock) {
        final var busId = BusUtils.busId(releaseLock.busNumber(), releaseLock.date());
        busStore.getAndUpdate(
                current -> current.releaseLock(releaseLock.numberOfSeats(), releaseLock.orderId()),
                busId
        );
    }

    public void handle(BusCommand.CancelConfirmation cancelConfirmation) {
        final var busId = BusUtils.busId(cancelConfirmation.busNumber(), cancelConfirmation.date());
        busStore.getAndUpdate(
                current -> current.cancelConfirmation(cancelConfirmation.numberOfSeats(), cancelConfirmation.orderId()),
                busId
        );
    }

    public void handle(BusCommand command) {
        if (command instanceof BusCommand.ConfirmLock confirmLock) {
            handle(confirmLock);
        } else if (command instanceof BusCommand.ReleaseLock releaseLock) {
            handle(releaseLock);
        } else if (command instanceof BusCommand.Lock lock) {
            handle(lock);
        } else if (command instanceof BusCommand.CancelConfirmation cancelConfirmation) {
            handle(cancelConfirmation);
        }
    }

    public void handle(BusRequests request) {
        if (request instanceof BusRequests.CreateBus create) {
            handle(create);
        }
    }

    public Bus get(String id) {
        return busStore.getEntity(id);
    }
}
