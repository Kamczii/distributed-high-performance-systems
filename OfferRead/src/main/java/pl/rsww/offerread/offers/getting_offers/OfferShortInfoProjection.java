package pl.rsww.offerread.offers.getting_offers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerread.projections.MongoProjection;
import pl.rsww.offerwrite.api.OfferIntegrationEvent;

import java.util.UUID;

import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_INTEGRATION_TOPIC;

@Component
@Slf4j
public class OfferShortInfoProjection extends MongoProjection<OfferShortInfo, UUID> {

    public OfferShortInfoProjection(OfferShortInfoRepository repository) {
        super(repository);
    }

    @KafkaListener(topics = OFFER_INTEGRATION_TOPIC, groupId = "OfferRead", containerFactory = "offerEventConsumerFactory",autoStartup = "${listen.auto.start:true}")
    public void listenOffer(OfferIntegrationEvent event) {
        log.info("Listener");
        if (event instanceof OfferIntegrationEvent.Created created) {
            log.info(event.toString());
            add(() -> mapToShortInfo(created));
        }
    }

    public static OfferShortInfo mapToShortInfo(OfferIntegrationEvent.Created event) {
        return new OfferShortInfo(
                event.offerId(),
                new OfferShortInfo.Hotel(
                        event.hotel().name(),
                        new OfferShortInfo.Room(
                                event.hotel().room().type(),
                                event.hotel().room().capacity(),
                                event.hotel().room().beds()
                        )
                ),
                new OfferShortInfo.Location(event.departure().city(), event.departure().country()),
                new OfferShortInfo.Location(event.destination().city(), event.destination().country()),
                event.start(),
                event.end(),
                event.status()
        );
    }
}
