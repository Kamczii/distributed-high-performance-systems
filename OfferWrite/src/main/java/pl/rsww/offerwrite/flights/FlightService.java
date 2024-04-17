package pl.rsww.offerwrite.flights;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rsww.dominik.api.FlightRequests;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final AggregateStore<Flight, FlightEvent, String> flightStore;

    public void create(FlightRequests.CreateFlight create) {
        flightStore.add(Flight.create(create));
    }
}
