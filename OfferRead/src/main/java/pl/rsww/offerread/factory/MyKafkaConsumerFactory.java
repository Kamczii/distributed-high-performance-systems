package pl.rsww.offerread.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import pl.rsww.offerwrite.api.OfferIntegrationEvent;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class MyKafkaConsumerFactory {
    private final ConsumerFactory<OfferIntegrationEvent> consumerFactory;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> offerEventConsumerFactory() {
        return consumerFactory.create(OfferIntegrationEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> locationEventConsumerFactory() {
        return consumerFactory.create(OfferIntegrationEvent.class);
    }


}
