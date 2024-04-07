package pl.rsww.offerwrite.flights;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.api.requests.FlightRequests;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final AggregateStore<Flight, FlightEvent, String> flightStore;

    void create(FlightRequests.CreateFlight create) {

    }
}
