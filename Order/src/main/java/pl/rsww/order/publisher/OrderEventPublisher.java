package pl.rsww.order.publisher;

import pl.rsww.order.event.order.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void publishOrderEvent(OrderEvent orderEvent) {
        kafkaTemplate.send("pl.rsww.order", orderEvent);
    }

}
