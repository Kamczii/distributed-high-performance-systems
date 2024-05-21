package pl.rsww.offerwrite.flights;

import com.eventstore.dbclient.EventStoreDBClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;
import pl.rsww.offerwrite.core.aggregates.AggregateStore;

@Configuration
public class FlightConfig {

    @Bean
    @ApplicationScope
    AggregateStore<Flight, FlightEvent, String> flightStore(EventStoreDBClient eventStore) {
        return new AggregateStore<>(
                eventStore,
                Flight::mapToStreamId,
                Flight::empty
        );
    }
}
