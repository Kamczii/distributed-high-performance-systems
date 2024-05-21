package pl.rsww.offerwrite.hotels;

import com.eventstore.dbclient.EventStoreDBClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;

import java.util.UUID;

@Configuration
public class HotelConfig {

    @Bean
    @ApplicationScope
    AggregateStore<Hotel, HotelEvent, UUID> hotelStore(EventStoreDBClient eventStore) {
        return new AggregateStore<>(
                eventStore,
                Hotel::mapToStreamId,
                Hotel::empty
        );
    }
}
