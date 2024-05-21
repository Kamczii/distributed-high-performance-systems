package pl.rsww.offerwrite.hotels.getting_hotel_rooms;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.core.events.EventEnvelope;
import pl.rsww.offerwrite.core.projections.EntityEventPublisher;
import pl.rsww.offerwrite.core.projections.JPAProjection;
import pl.rsww.offerwrite.hotels.HotelEvent;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.location.LocationRepository;

import java.util.Optional;
import java.util.UUID;

@Component
class HotelProjection extends JPAProjection<Hotel, UUID> {

  private final LocationRepository locationRepository;

  protected HotelProjection(HotelRepository repository,
                            LocationRepository locationRepository,
                            EntityEventPublisher entityEventPublisher) {
    super(repository, entityEventPublisher);
    this.locationRepository = locationRepository;
  }

  @EventListener
  void handleHotelCreated(EventEnvelope<HotelEvent.HotelCreated> eventEnvelope) {
    add(eventEnvelope, () -> {
      var event = eventEnvelope.data();

      final var location = getLocation(event.location());
      var hotel = new Hotel();
      hotel.setId(event.hotelId());
      hotel.setName(event.name());
      hotel.setLocation(location);
      hotel.setVersion(eventEnvelope.metadata().streamPosition());
      hotel.setLastProcessedPosition(eventEnvelope.metadata().logPosition());
      event.rooms().rooms()
              .stream()
              .map(hotelRoom -> HotelRoom.builder().type(hotelRoom.type()).capacity(hotelRoom.capacity()).beds(hotelRoom.beds()).build())
              .forEach(hotel::addHotelRoom);
      return hotel;
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
