package pl.rsww.notifications.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;
import pl.rsww.payment.api.PaymentEvent;


@EnableKafka
@Configuration
@RequiredArgsConstructor
public class MyKafkaConsumerFactory {
    private final ConsumerFactory<OfferIntegrationEvent> offerConsumerFactory;
    private final ConsumerFactory<PaymentEvent> paymentConsumerFactory;


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> offerEventConsumerFactory() {
        return offerConsumerFactory.create(OfferIntegrationEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> paymentEventConsumerFactory() {
        return paymentConsumerFactory.create(PaymentEvent.class);
    }


}