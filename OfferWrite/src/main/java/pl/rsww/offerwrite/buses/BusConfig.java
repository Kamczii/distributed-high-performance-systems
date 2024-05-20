package pl.rsww.offerwrite.buses;

import com.eventstore.dbclient.EventStoreDBClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;

@Configuration
public class BusConfig {

    @Bean
    @ApplicationScope
    AggregateStore<Bus, BusEvent, String> busStore(EventStoreDBClient eventStore) {
        return new AggregateStore<>(
                eventStore,
                Bus::mapToStreamId,
                Bus::empty
        );
    }
}
