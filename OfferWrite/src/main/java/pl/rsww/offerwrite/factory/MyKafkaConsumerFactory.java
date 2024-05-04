package pl.rsww.offerwrite.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import pl.rsww.dominik.api.FlightRequests;
import pl.rsww.dominik.api.HotelRequests;
import pl.rsww.offerwrite.api.requests.OfferRequests;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class MyKafkaConsumerFactory {
    private final ConsumerFactory<FlightRequests> flightRequestsConsumerFactory;
    private final ConsumerFactory<HotelRequests> hotelRequestsConsumerFactory;
    private final ConsumerFactory<OfferRequests> offerRequestsConsumerFactory;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> flightConsumerFactory() {
        return flightRequestsConsumerFactory.create(FlightRequests.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> hotelConsumerFactory() {
        return hotelRequestsConsumerFactory.create(HotelRequests.class);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> offerConsumerFactory() {
        return offerRequestsConsumerFactory.create(OfferRequests.class);
    }
}
