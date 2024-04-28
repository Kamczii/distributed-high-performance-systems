package pl.rsww.offerwrite.core.projections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import pl.rsww.offerwrite.core.events.EventEnvelope;
import pl.rsww.offerwrite.core.views.VersionedView;

import java.util.function.Function;
import java.util.function.Supplier;


public abstract class JPAProjection<View extends IdentifiableEntity, Id> {
  private final CrudRepository<View, Id> repository;
  private final Logger logger = LoggerFactory.getLogger(JPAProjection.class);
  private final EntityEventPublisher entityEventPublisher;

  protected JPAProjection(CrudRepository<View, Id> repository, EntityEventPublisher entityEventPublisher) {
    this.repository = repository;
    this.entityEventPublisher = entityEventPublisher;
  }

  protected <Event> void add(EventEnvelope<Event> eventEnvelope, Supplier<View> handle) {
    var result = handle.get();

    if(result instanceof VersionedView versionedView){
      versionedView.setMetadata(eventEnvelope.metadata());
    }

    var save = repository.save(result);
    entityEventPublisher.publish(save);
  }

  protected <Event> void getAndUpdate(
    Id viewId,
    EventEnvelope<Event> eventEnvelope,
    Function<View, View> handle
  ) {
    var view = repository.findById(viewId);

    if (view.isEmpty()) {
      logger.warn("View with id %s was not found for event %s".formatted(viewId, eventEnvelope.metadata().eventType()));
      return;
    }

    if (view.get() instanceof VersionedView versionedView && wasAlreadyApplied(versionedView, eventEnvelope)) {
      logger.warn("View with id %s was already applied for event %s".formatted(viewId, eventEnvelope.metadata().eventType()));
      return;
    }

    var result = handle.apply(view.get());

    if(result instanceof VersionedView versionedView){
      versionedView.setMetadata(eventEnvelope.metadata());
    }

    repository.save(result);
  }

  protected void deleteById(Id viewId) {
    repository.deleteById(viewId);
  }

  private static boolean wasAlreadyApplied(VersionedView view, EventEnvelope<?> eventEnvelope) {
    return view.getLastProcessedPosition() >= eventEnvelope.metadata().logPosition();
  }
}
