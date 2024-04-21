package pl.rsww.payment.publisher;

import pl.rsww.payment.api.PaymentIntegrationEvent;
import pl.rsww.payment.event.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventPublisher {

    @Autowired
    private KafkaTemplate<String, PaymentIntegrationEvent> kafkaTemplate;

    public void publishPaymentEvent(PaymentIntegrationEvent paymentEvent) {
        kafkaTemplate.send("pl.rsww.payment", paymentEvent);
    }

}
