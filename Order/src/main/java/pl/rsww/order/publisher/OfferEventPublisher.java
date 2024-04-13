package pl.rsww.order.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.rsww.order.event.offer.OfferEvent;

@Component
public class OfferEventPublisher {

    @Autowired
    private KafkaTemplate<String, OfferEvent> kafkaTemplate;

    public void publishOfferEvent(OfferEvent offerEvent) {
        kafkaTemplate.send("pl.rsww.offer", offerEvent);
    }

}
