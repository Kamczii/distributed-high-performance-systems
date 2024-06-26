package pl.rsww.offerwrite.flights;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.rsww.tour_operator.api.FlightRequests;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {
    private final AggregateStore<Flight, FlightEvent, String> flightStore;

    public void handle(FlightRequests.CreateFlight create) {
        try {
            Flight exist = flightStore.getEntity(FlightUtils.flightId(create.flightNumber(), create.date()));
        } catch (EntityNotFoundException e) {
            flightStore.add(Flight.create(create));
        }
    }

    public void handle(FlightCommand.ConfirmLock create) {
        final var flightId = FlightUtils.flightId(create.flightNumber(), create.date());
        flightStore.getAndUpdate(
                current -> current.confirmLock(create.numberOfSeats(), create.orderId()),
                flightId
        );
    }

    public void handle(FlightCommand.Lock create) {
        final var flightId = FlightUtils.flightId(create.flightNumber(), create.date());
        flightStore.getAndUpdate(
                current -> current.lock(create.numberOfSeats(), create.orderId()),
                flightId
        );
    }

    public void handle(FlightCommand.ReleaseLock releaseLock) {
        final var flightId = FlightUtils.flightId(releaseLock.flightNumber(), releaseLock.date());
        flightStore.getAndUpdate(
                current -> current.releaseLock(releaseLock.numberOfSeats(), releaseLock.orderId()),
                flightId
        );
    }

    public void handle(FlightCommand.CancelConfirmation cancelConfirmation) {
        final var flightId = FlightUtils.flightId(cancelConfirmation.flightNumber(), cancelConfirmation.date());
        flightStore.getAndUpdate(
                current -> current.cancelConfirmation(cancelConfirmation.numberOfSeats(), cancelConfirmation.orderId()),
                flightId
        );
    }

    public void handle(FlightCommand command) {
        if (command instanceof FlightCommand.ConfirmLock confirmLock) {
            handle(confirmLock);
        } else if (command instanceof FlightCommand.ReleaseLock releaseLock) {
            handle(releaseLock);
        } else if (command instanceof FlightCommand.Lock lock) {
            handle(lock);
        } else if (command instanceof FlightCommand.CancelConfirmation cancelConfirmation) {
            handle(cancelConfirmation);
        }
    }

    public void handle(FlightRequests request) {
        if (request instanceof FlightRequests.CreateFlight create) {
            handle(create);
        }
    }

    public Flight get(String id) {
        return flightStore.getEntity(id);
    }
}
