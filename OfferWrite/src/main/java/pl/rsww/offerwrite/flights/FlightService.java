package pl.rsww.offerwrite.flights;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rsww.dominik.api.FlightRequests;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final AggregateStore<Flight, FlightEvent, String> flightStore;

    public void handle(FlightRequests.CreateFlight create) {
        flightStore.add(Flight.create(create));
    }

    public void handle(FlightRequests.SeatConfirmed create) {
        final var flightId = FlightUtils.flightId(create.flightNumber(), create.date());
        flightStore.getAndUpdate(
                current -> current.confirmReservation(create.numberOfSeats(), create.orderId()),
                flightId
        );
    }

    public void handle(FlightRequests.SeatReserved create) {
        final var flightId = FlightUtils.flightId(create.flightNumber(), create.date());
        flightStore.getAndUpdate(
                current -> current.reserveSeats(create.numberOfSeats(), create.orderId()),
                flightId
        );
    }
}
