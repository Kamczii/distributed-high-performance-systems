package pl.rsww.offerwrite.core.events;

public interface EventBus {
  <Event> void publish(EventEnvelope<Event> event);
}
