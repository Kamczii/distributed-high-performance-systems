package pl.rsww.offerread.listeners;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import pl.rsww.offerread.event.OfferingEvent;

@Slf4j
public class OfferEventListener {

    @KafkaListener(topics = "pl.rsww.offer", groupId = "OfferRead")
    public void listenOffer(OfferingEvent event) {
        log.info(event.getClass() + " arrived");
    }
}
