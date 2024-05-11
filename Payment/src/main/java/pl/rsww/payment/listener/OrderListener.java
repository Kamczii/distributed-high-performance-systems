package pl.rsww.payment.listener;

import pl.rsww.order.api.OrderIntegrationEvent;
import pl.rsww.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderListener {

    @Autowired
    PaymentService paymentService;

    @KafkaListener(topics = "pl.rsww.order")
    public void handleOrderEvent(OrderIntegrationEvent orderEvent) {
        log.info("Received ORDER EVENT: {}", orderEvent.toString());
        if (orderEvent.eventType() == OrderIntegrationEvent.EventType.CREATED) {
            processOrderCreatedEvent(orderEvent);
        }

    }

    private void processOrderCreatedEvent(OrderIntegrationEvent orderCreatedEvent) {
        paymentService.createPayment(orderCreatedEvent);
    }

}
