package pl.rsww.offerwrite.flights;

import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;

public class Flight extends AbstractAggregate<FlightEvent, String> {

    @Override
    public void when(FlightEvent flightEvent) {

    }

    public static Flight empty() {
        return new Flight();
    }

    static String mapToStreamId(String flightNumber) {
        return "Flight-%s".formatted(flightNumber);
    }
}
