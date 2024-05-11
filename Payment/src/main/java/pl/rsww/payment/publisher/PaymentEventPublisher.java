package pl.rsww.payment.publisher;

import lombok.extern.slf4j.Slf4j;
import pl.rsww.payment.api.PaymentIntegrationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentEventPublisher {

    @Autowired
    private KafkaTemplate<String, PaymentIntegrationEvent> kafkaTemplate;

    public void publishPaymentEvent(PaymentIntegrationEvent paymentEvent) {
        log.info(String.format("Kafka produce on pl.rsww.payment: %s", paymentEvent.toString()));
        kafkaTemplate.send("pl.rsww.payment", paymentEvent);
    }

}
