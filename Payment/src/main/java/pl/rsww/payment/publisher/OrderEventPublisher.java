package pl.rsww.payment.publisher;

import pl.rsww.order.api.OrderIntegrationEvent;
import pl.rsww.payment.event.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    @Autowired
    private KafkaTemplate<String, OrderIntegrationEvent> kafkaTemplate;

    public void publishOrderEvent(OrderIntegrationEvent orderEvent) {
        kafkaTemplate.send("pl.rsww.order", orderEvent);
    }

}
