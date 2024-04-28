package pl.rsww.order.publisher;

import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.rsww.order.api.OrderIntegrationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderEventPublisher {

    @Autowired
    private KafkaTemplate<String, OrderIntegrationEvent> kafkaTemplate;

    public void publishOrderEvent(OrderIntegrationEvent orderEvent) {
        log.info(String.format("Kafka produce on pl.rsww.order: %s", orderEvent.toString()));
        kafkaTemplate.send("pl.rsww.order", orderEvent);
    }

}
