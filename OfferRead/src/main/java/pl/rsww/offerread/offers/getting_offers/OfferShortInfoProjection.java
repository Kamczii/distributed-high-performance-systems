package pl.rsww.offerread.offers.getting_offers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerread.mapper.AgeRangePriceMapper;
import pl.rsww.offerread.projections.MongoProjection;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;

import java.util.UUID;

import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_INTEGRATION_TOPIC;

@Component
@Slf4j
public class OfferShortInfoProjection extends MongoProjection<OfferShortInfo, UUID> {
    private final AgeRangePriceMapper ageRangePriceMapper;
    public OfferShortInfoProjection(OfferShortInfoRepository repository, AgeRangePriceMapper ageRangePriceMapper) {
        super(repository);
        this.ageRangePriceMapper = ageRangePriceMapper;
    }

    @KafkaListener(topics = OFFER_INTEGRATION_TOPIC, groupId = "OfferRead", containerFactory = "offerEventConsumerFactory",autoStartup = "${listen.auto.start:true}")
    public void listenOffer(OfferIntegrationEvent event) {
        log.info("Listener");
        if (event instanceof OfferIntegrationEvent.Created created) {
            add(() -> mapToShortInfo(created));
        } else if (event instanceof OfferIntegrationEvent.StatusChanged statusChanged) {
            getAndUpdate(statusChanged.offerId(), current -> current.changeStatus(statusChanged.status()));
        }
    }

    public OfferShortInfo mapToShortInfo(OfferIntegrationEvent.Created event) {
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
                event.status(),
                ageRangePriceMapper.map(event.priceList())
        );
    }


}
