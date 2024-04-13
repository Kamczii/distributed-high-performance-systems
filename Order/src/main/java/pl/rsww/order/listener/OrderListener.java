package pl.rsww.order.listener;

import pl.rsww.order.event.order.OrderCancelledEvent;
import pl.rsww.order.event.order.OrderEvent;
import pl.rsww.order.service.OrderService;
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
    public void handleOrderEvent(OrderEvent orderEvent) {

        if (orderEvent instanceof OrderCancelledEvent) {
            processOrderCancelledEvent((OrderCancelledEvent) orderEvent);
        }

    }

    private void processOrderCancelledEvent(OrderCancelledEvent orderCancelledEvent) {
        orderService.rejectOrder(orderCancelledEvent);
    }



}
