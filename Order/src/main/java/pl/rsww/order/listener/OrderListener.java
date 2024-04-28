package pl.rsww.order.listener;

import pl.rsww.order.service.OrderService;
import pl.rsww.order.api.OrderIntegrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderListener {

    @Autowired
    OrderService orderService;

    @KafkaListener(topics = "pl.rsww.order")
    public void handleOrderEvent(OrderIntegrationEvent orderEvent) {

        if (orderEvent.eventType().equals(OrderIntegrationEvent.EventType.CANCELLED)) {
            processOrderCancelledEvent(orderEvent);
        }

    }

    private void processOrderCancelledEvent(OrderIntegrationEvent orderCancelledEvent) {
        orderService.rejectOrder(orderCancelledEvent);
    }



}
