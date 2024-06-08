package pl.rsww.payment.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.OfferWriteTopics;
import pl.rsww.offerwrite.api.response.OfferResponse;
import pl.rsww.order.api.OrderEvent;
import pl.rsww.order.api.OrderTopics;
import pl.rsww.payment.service.PaymentService;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPendingListener {
    private final PaymentService paymentService;

    @KafkaListener(topics = OrderTopics.ORDER_BASIC_TOPIC, autoStartup = "${listen.auto.start:true}", containerFactory = "pendingOrderFactory")
    public void listenOrder(OrderEvent orderEvent) {
        if (Objects.requireNonNull(orderEvent) instanceof OrderEvent.Pending pendingOrder) {
            paymentService.createPayment(pendingOrder.orderId(), pendingOrder.userId(), pendingOrder.price(), pendingOrder.location(), pendingOrder.hotel(), pendingOrder.room());
        } else {
            log.info(String.format("Ignored order event: %s", orderEvent));
        }
    }
}
