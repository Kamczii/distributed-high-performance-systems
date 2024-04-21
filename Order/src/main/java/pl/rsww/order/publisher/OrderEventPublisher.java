package pl.rsww.order.publisher;

import pl.rsww.order.api.OrderIntegrationEvent;
import pl.rsww.order.event.order.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.rsww.order.model.Order;

@Component
public class OrderEventPublisher {

    @Autowired
    private KafkaTemplate<String, OrderIntegrationEvent> kafkaTemplate;

    public void publishOrderEvent(OrderIntegrationEvent orderEvent) {
        kafkaTemplate.send("pl.rsww.order", orderEvent);
    }

}
