package pl.rsww.offerwrite.buses.getting_bus_seats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.buses.BusEvent;
import pl.rsww.offerwrite.buses.BusRepository;
import pl.rsww.offerwrite.core.events.EventEnvelope;
import pl.rsww.offerwrite.core.projections.EntityEventPublisher;
import pl.rsww.offerwrite.core.projections.JPAProjection;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.location.LocationRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
class BusProjection extends JPAProjection<Bus, UUID> {
    private final LocationRepository locationRepository;

    protected BusProjection(BusRepository repository,
                            LocationRepository locationRepository,
                            EntityEventPublisher entityEventPublisher) {
        super(repository, entityEventPublisher);
        this.locationRepository = locationRepository;
    }

    @EventListener
    void handleBusCreated(EventEnvelope<BusEvent.BusCreated> eventEnvelope) {
        add(eventEnvelope, () -> {
            var event = eventEnvelope.data();

            final var departure = getLocation(event.departure());
            final var destination = getLocation(event.destination());

            var bus = new Bus();
            bus.setBusNumber(event.busNumber());
            bus.setDate(event.date());
            bus.setDeparture(departure);
            bus.setDestination(destination);
            bus.setVersion(eventEnvelope.metadata().streamPosition());
            bus.setLastProcessedPosition(eventEnvelope.metadata().logPosition());
            return bus;
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
}
