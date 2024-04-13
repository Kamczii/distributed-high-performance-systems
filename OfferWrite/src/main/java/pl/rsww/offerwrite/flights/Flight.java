package pl.rsww.offerwrite.flights;

import lombok.Getter;
import pl.rsww.offerwrite.api.requests.FlightRequests;
import pl.rsww.offerwrite.common.location.Location;
import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Flight extends AbstractAggregate<FlightEvent, String> {
    private Location departure;
    private Location destination;
    private String flightNumber;
    private LocalDate date;
    private Integer capacity;

    Flight() {
    }

    Flight(
            Location departure,
            Location destination,
            String flightNumber,
            LocalDate date,
            Integer capacity
    ) {
        enqueue(new FlightEvent.FlightCreated(departure, destination, flightNumber, date, capacity));
    }

    public static Flight create(FlightRequests.CreateFlight create) {
        var departure = new Location(create.departure().country(), create.departure().city());
        var destination = new Location(create.destination().country(), create.destination().city());
        var flightNumber = create.flightNumber();
        var date = create.date();
        var capacity = create.numberOfSeats();

        return new Flight(departure, destination, flightNumber, date, capacity);
    }

    @Override
    public void when(FlightEvent event) {
        switch (event) {
            case FlightEvent.FlightCreated flightCreated -> {
                id = UUID.randomUUID().toString();
                departure = flightCreated.departure();
                destination = flightCreated.destination();
                flightNumber = flightCreated.flightNumber();
                date = flightCreated.date();
                capacity = flightCreated.capacity();
            }
        }
    }

    public static Flight empty() {
        return new Flight();
    }

    static String mapToStreamId(String id) {
        return "Flight-%s".formatted(id);
    }
}
