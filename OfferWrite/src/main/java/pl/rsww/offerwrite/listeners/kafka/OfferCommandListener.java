package pl.rsww.offerwrite.listeners.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.OfferWriteTopics;
import pl.rsww.offerwrite.api.command.OfferCommand;
import pl.rsww.offerwrite.offer.OfferService;

@Component
@RequiredArgsConstructor
public class OfferCommandListener {
    private final OfferService offerService;

    @KafkaListener(topics = OfferWriteTopics.OFFER_COMMAND_TOPIC, groupId = "OfferWrite", containerFactory = "flightConsumerFactory",autoStartup = "${listen.auto.start:true}")
    public void listenOffer(OfferCommand offerCommand) {
        try {
            switch (offerCommand) {
                case OfferCommand.Lock lock -> {
                    offerService.reserveOffer(lock.offerId(), lock.orderId(), lock.ageOfVisitors());
                }
                case OfferCommand.ConfirmLock confirmLock -> {
                    offerService.confirmOffer(confirmLock.offerId(), confirmLock.orderId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
