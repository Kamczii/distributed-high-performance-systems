package pl.rsww.offerwrite.core.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public record EventForwarder(
  ApplicationEventPublisher applicationEventPublisher
) implements EventBus {

  @Override
  public <Event> void publish(EventEnvelope<Event> event) {
    log.info("Published " + event.data().getClass());
    applicationEventPublisher.publishEvent(event);
  }
}
