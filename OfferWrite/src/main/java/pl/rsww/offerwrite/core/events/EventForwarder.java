package pl.rsww.offerwrite.core.events;

import org.springframework.context.ApplicationEventPublisher;

public record EventForwarder(
  ApplicationEventPublisher applicationEventPublisher
) implements EventBus {

  @Override
  public <Event> void publish(EventEnvelope<Event> event) {
    applicationEventPublisher.publishEvent(event);
  }
}
