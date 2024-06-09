package pl.rsww.order.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.OfferWriteTopics;
import pl.rsww.offerwrite.api.command.OfferCommand;
import pl.rsww.offerwrite.api.response.OfferResponse;
import pl.rsww.order.service.OrderService;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OfferResponseListener {
    private final OrderService orderService;

    @KafkaListener(topics = OfferWriteTopics.OFFER_RESPONSE_TOPIC, containerFactory = "lockFactory")
    public void listenOffer(OfferResponse offerResponse) {
        if (Objects.requireNonNull(offerResponse) instanceof OfferResponse.Lock lock) {
            log.info(String.format("Received offer response event: %s", offerResponse));
            orderService.setOrderPrice(lock.orderId(), lock.price(), lock.status(), lock.location(), lock.hotel(), lock.hotel().room());
        } else {
            log.info(String.format("Ignored offer response event: %s", offerResponse));
        }
    }
}
