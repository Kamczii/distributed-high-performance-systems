package pl.rsww.offerwrite.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.rsww.offerwrite.core.events.EventBus;
import pl.rsww.offerwrite.core.events.EventForwarder;
import pl.rsww.offerwrite.core.serialization.EventSerializer;

@Configuration
class CoreConfig {
  @Bean
  ObjectMapper defaultJSONMapper() {
    return EventSerializer.mapper;
  }

  @Bean
  EventBus eventBus(ApplicationEventPublisher applicationEventPublisher) {
    return new EventForwarder(applicationEventPublisher);
  }
}
