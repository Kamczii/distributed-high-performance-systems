package pl.rsww.offerwrite.listeners.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.core.events.EventEnvelope;
import pl.rsww.offerwrite.producer.ObjectRequestKafkaProducer;

@Component
@RequiredArgsConstructor
public class InternalEventBus {
//    private final ObjectRequestKafkaProducer objectRequestKafkaProducer;

    @EventListener
    public <Event> void onEventSendKafkaMessage(EventEnvelope<Event> event) {
//        objectRequestKafkaProducer.produce("pl.rsww.offer", event);
    }

}
