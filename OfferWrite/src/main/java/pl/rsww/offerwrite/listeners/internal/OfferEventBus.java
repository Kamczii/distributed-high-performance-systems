package pl.rsww.offerwrite.listeners.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.rsww.offerwrite.api.OfferIntegrationEvent;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferEvent;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;
import pl.rsww.offerwrite.producer.ObjectRequestKafkaProducer;

@Slf4j
@Component
@RequiredArgsConstructor
public class OfferEventBus {
    private final ObjectRequestKafkaProducer objectRequestKafkaProducer;
    private final OfferRepository offerRepository;

    @EventListener
    public void onEvent(OfferEvent event) {
        handle(event);
    }

    @TransactionalEventListener
    public void onTransactionalEvent(OfferEvent event) {
        handle(event);
    }

    private void handle(OfferEvent offerEvent) {
        log.info(offerEvent.getEntityId() + " changed");
        var offer = offerRepository.findById(offerEvent.getEntityId())
                .orElseThrow();

        var departure = mapLocation(offer.getInitialFlight().getDeparture());
        var destination = mapLocation(offer.getInitialFlight().getDestination());
        var room = mapRoom(offer);
        var hotel = mapHotel(offer, room);
        var start = offer.getInitialFlight().getDate();
        var end = offer.getReturnFlight().getDate();
        var event = new OfferIntegrationEvent.Created(offer.getId(), hotel, departure, destination, start, end);
        publish(event);
    }

    private void publish(OfferIntegrationEvent event) {
        log.info(event.toString()); //todo kafka
        objectRequestKafkaProducer.produce("pl.rsww.offerwrite.offer", event);
    }

    private static OfferIntegrationEvent.Hotel mapHotel(Offer offer, OfferIntegrationEvent.Room room) {
        return new OfferIntegrationEvent.Hotel(offer.getHotelRoom().getHotel().getName(), room);
    }

    private static OfferIntegrationEvent.Location mapLocation(Location offer) {
        return new OfferIntegrationEvent.Location(offer.getCity(), offer.getCountry());
    }

    private static OfferIntegrationEvent.Room mapRoom(Offer offer) {
        return new OfferIntegrationEvent.Room(offer.getHotelRoom().getType(), offer.getHotelRoom().getCapacity(), offer.getHotelRoom().getBeds());
    }

}
