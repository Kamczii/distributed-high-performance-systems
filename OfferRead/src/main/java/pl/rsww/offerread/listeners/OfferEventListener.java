package pl.rsww.offerread.listeners;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.OfferIntegrationEvent;

@Slf4j
@Component
public class OfferEventListener {

    @KafkaListener(topics = "pl.rsww.offerwrite.offer", groupId = "OfferRead", containerFactory = "offerEventConsumerFactory")
    public void listenOffer(OfferIntegrationEvent event) {
        log.info(event.toString());
    }
}
