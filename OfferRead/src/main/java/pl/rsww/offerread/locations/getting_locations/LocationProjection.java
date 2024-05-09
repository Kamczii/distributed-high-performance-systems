package pl.rsww.offerread.locations.getting_locations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerread.projections.MongoProjection;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;

import java.util.UUID;

import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_INTEGRATION_TOPIC;

@Slf4j
@Component
public class LocationProjection extends MongoProjection<Location, UUID> {

    public LocationProjection(LocationRepository repository) {
        super(repository);
    }

    @KafkaListener(topics = OFFER_INTEGRATION_TOPIC, groupId = "OfferRead2", containerFactory = "locationEventConsumerFactory",autoStartup = "${listen.auto.start:true}")
    public void listenOffer(OfferIntegrationEvent event) {
        log.info("Listener");
        if (event instanceof OfferIntegrationEvent.Created created) {
            log.info(event.toString());
            add(() -> map(created.departure()));
            add(() -> map(created.destination()));
        }
    }

    public static Location map(OfferIntegrationEvent.Location location) {
        return new Location(location.city(), location.country());
    }
}
