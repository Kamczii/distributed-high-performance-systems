package pl.rsww.payment.publisher;

import pl.rsww.payment.event.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventPublisher {

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void publishPaymentEvent(PaymentEvent paymentEvent) {
        kafkaTemplate.send("pl.rsww.payment", paymentEvent);
    }

}
