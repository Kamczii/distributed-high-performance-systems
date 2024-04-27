package pl.rsww.offerread.locations.getting_locations;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfo;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfoRepository;
import pl.rsww.offerread.projections.MongoProjection;
import pl.rsww.offerwrite.api.OfferIntegrationEvent;

import java.util.UUID;

import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_INTEGRATION_TOPIC;

@Component
public class LocationProjection extends MongoProjection<Location, UUID> {

    public LocationProjection(LocationRepository repository) {
        super(repository);
    }

    @KafkaListener(topics = OFFER_INTEGRATION_TOPIC, groupId = "OfferRead", containerFactory = "offerEventConsumerFactory",autoStartup = "${listen.auto.start:true}")
    public void listenOffer(OfferIntegrationEvent event) {
        if (event instanceof OfferIntegrationEvent.Created created) {
            add(() -> map(created.departure()));
            add(() -> map(created.destination()));
        }
    }

    public static Location map(OfferIntegrationEvent.Location location) {
        return new Location(location.city(), location.country());
    }
}
