package pl.rsww.payment.listener;

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
    public void handleOrderEvent(OrderEvent orderEvent) {

        if (orderEvent instanceof OrderCreatedEvent) {
            processOrderCreatedEvent((OrderCreatedEvent) orderEvent);
        }

    }

    private void processOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        log.info("Received order event with id: {}", orderCreatedEvent.getOrderId());

        paymentService.createPayment(orderCreatedEvent);
    }

}
