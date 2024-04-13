package pl.rsww.offerwrite.flights.getting_flight_seats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.core.events.EventEnvelope;
import pl.rsww.offerwrite.core.projections.JPAProjection;
import pl.rsww.offerwrite.flights.FlightEvent;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.location.LocationRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
class FlightProjection extends JPAProjection<Flight, UUID> {
    private final LocationRepository locationRepository;
    private final SeatStateRepository seatStateRepository;

    protected FlightProjection(FlightRepository repository,
                               LocationRepository locationRepository, SeatStateRepository seatStateRepository) {
        super(repository);
        this.locationRepository = locationRepository;
        this.seatStateRepository = seatStateRepository;
    }

    @EventListener
    void handleFlightCreated(EventEnvelope<FlightEvent.FlightCreated> eventEnvelope) {
        add(eventEnvelope, () -> {
            var event = eventEnvelope.data();

            final var departure = getLocation(event.departure());
            final var destination = getLocation(event.destination());

            var flight = new Flight();
            flight.setFlightNumber(event.flightNumber());
            flight.setDate(event.date());
            flight.setDeparture(departure);
            flight.setDestination(destination);
            var openState = seatStateRepository.findByState(AvailableSeatState.OPEN);
            for (var i = 0; i < event.capacity(); i++) {
                var seat = this.createSeat(openState);
                flight.addSeat(seat);
            }
            flight.setVersion(eventEnvelope.metadata().streamPosition());
            flight.setLastProcessedPosition(eventEnvelope.metadata().logPosition());
            return flight;
        });
    }

    private Location getLocation(pl.rsww.offerwrite.common.location.Location location) {
        return findLocation(location)
                .orElseGet(() -> createLocation(location));
    }

    private Location createLocation(pl.rsww.offerwrite.common.location.Location location) {
        var entity = Location.builder().city(location.city()).country(location.country()).build();
        return locationRepository.save(entity);
    }

    private Optional<Location> findLocation(pl.rsww.offerwrite.common.location.Location location) {
        return locationRepository.findByCityAndCountry(location.city(), location.country());
    }

    private FlightSeat createSeat(SeatState state) {
        return FlightSeat.builder()
                .seatState(state)
                .build();
    }
}
