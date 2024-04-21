package pl.rsww.payment.listener;

import pl.rsww.order.api.OrderIntegrationEvent;
import pl.rsww.payment.event.OrderCreatedEvent;
import pl.rsww.payment.event.OrderEvent;
//import com.rsww.project.event.OrderPendingEvent;
//import com.rsww.project.service.OrderService;
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

        if (orderEvent.eventType().equals(OrderIntegrationEvent.EventType.CANCELLED)) {
            processOrderCreatedEvent(orderEvent);
        }

    }

    private void processOrderCreatedEvent(OrderIntegrationEvent orderCreatedEvent) {
        log.info("Received order event with id: {}", orderCreatedEvent.orderId());

        paymentService.createPayment(orderCreatedEvent);
    }

}
