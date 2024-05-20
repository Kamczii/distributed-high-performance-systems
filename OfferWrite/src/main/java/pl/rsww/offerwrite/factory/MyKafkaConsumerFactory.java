package pl.rsww.offerwrite.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import pl.rsww.tour_operator.api.BusRequests;
import pl.rsww.tour_operator.api.FlightRequests;
import pl.rsww.tour_operator.api.HotelRequests;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class MyKafkaConsumerFactory {
    private final ConsumerFactory<FlightRequests> flightRequestsConsumerFactory;
    private final ConsumerFactory<BusRequests> busRequestsConsumerFactory;
    private final ConsumerFactory<HotelRequests> hotelRequestsConsumerFactory;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> flightConsumerFactory() {
        return flightRequestsConsumerFactory.create(FlightRequests.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> busConsumerFactory() {
        return busRequestsConsumerFactory.create(BusRequests.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> hotelConsumerFactory() {
        return hotelRequestsConsumerFactory.create(HotelRequests.class);
    }
}
