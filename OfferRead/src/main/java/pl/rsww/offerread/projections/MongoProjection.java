package pl.rsww.offerread.projections;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.repository.MongoRepository;
import pl.rsww.offerread.events.EventEnvelope;
import pl.rsww.offerread.views.VersionedView;

import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class MongoProjection<View, Id> {
  private final MongoRepository<View, Id> repository;
  private final Logger logger = LoggerFactory.getLogger(JPAProjection.class);


  protected void add(Supplier<View> handle) {
    var result = handle.get();

    try {
      repository.save(result);
    } catch (DuplicateKeyException e) {
      logger.info(result.toString() + " already exists");
    }
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
//      versionedView.setMetadata(eventEnvelope.metadata()); todo
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
