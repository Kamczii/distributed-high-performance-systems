package pl.rsww.order.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    public void publish(String topic, Object data) {
        log.info(String.format("Kafka produce on %s: %s", topic, data.toString()));
        kafkaTemplate.send(topic, data);
    }
}
