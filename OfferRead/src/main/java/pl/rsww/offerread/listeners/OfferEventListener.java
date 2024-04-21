package pl.rsww.offerread.listeners;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfoProjection;
import pl.rsww.offerwrite.api.OfferIntegrationEvent;

import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_INTEGRATION_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class OfferEventListener {
    private final OfferShortInfoProjection offerShortInfoProjection;


}
