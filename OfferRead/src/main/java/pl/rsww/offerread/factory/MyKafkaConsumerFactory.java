package pl.rsww.offerread.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import pl.rsww.offerread.event.OfferingEvent;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class MyKafkaConsumerFactory {
    private final ConsumerFactory<OfferingEvent> consumerFactory;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> offerEventConsumerFactory() {
        return consumerFactory.create(OfferingEvent.class);
    }


}
