package pl.rsww.offerwrite.core.projections;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    void publish(Identifiable entity) {
        applicationEventPublisher.publishEvent(entity);
    }

}
