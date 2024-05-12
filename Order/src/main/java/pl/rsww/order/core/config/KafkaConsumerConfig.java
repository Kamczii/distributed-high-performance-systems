package pl.rsww.order.core.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import pl.rsww.offerwrite.api.response.OfferResponse;
import pl.rsww.order.core.deserializer.GenericObjectDeserializer;
import pl.rsww.payment.api.PaymentEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public <T> ConsumerFactory<String, T> consumerFactory(Class<T> targetType) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(targetType);
        deserializer.addTrustedPackages("pl.rsww.offerwrite.api.response");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(Class<T> targetType) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(targetType));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OfferResponse.Lock> lockFactory() {
        return kafkaListenerContainerFactory(OfferResponse.Lock.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent.Success> paymentSuccessFactory() {
        return kafkaListenerContainerFactory(PaymentEvent.Success.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent.Fail> paymentFailFactory() {
        return kafkaListenerContainerFactory(PaymentEvent.Fail.class);
    }

}
