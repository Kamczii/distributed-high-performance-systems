package pl.rsww.offerwrite.core.aggregates;

import com.eventstore.dbclient.*;
import jakarta.persistence.EntityNotFoundException;
import pl.rsww.offerwrite.core.http.ETag;
import pl.rsww.offerwrite.core.serialization.EventSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AggregateStore<Entity extends AbstractAggregate<Event, Id>, Event, Id> {
  private final EventStoreDBClient eventStore;
  private final Function<Id, String> mapToStreamId;
  private final Supplier<Entity> getEmpty;

  public AggregateStore(
    EventStoreDBClient eventStore,
    Function<Id, String> mapToStreamId,
    Supplier<Entity> getEmpty
  ) {

    this.eventStore = eventStore;
    this.mapToStreamId = mapToStreamId;
    this.getEmpty = getEmpty;
  }

  Optional<Entity> get(Id id) {
    var streamId = mapToStreamId.apply(id);

    var events = getEvents(streamId);

    if (events.isEmpty())
      return Optional.empty();

    var current = getEmpty.get();

    for (var event : events.get()) {
      current.when(event);
    }

    return Optional.ofNullable(current);
  }

  public ETag add(Entity entity) {
    return appendEvents(
      entity,
      AppendToStreamOptions.get().expectedRevision(ExpectedRevision.noStream())
    );
  }

  public ETag getAndUpdate(
    Consumer<Entity> handle,
    Id id
  ) {
    var entity = getEntity(id);

    handle.accept(entity);

    return appendEvents(entity, AppendToStreamOptions.get());
  }

  public Entity getEntity(Id id) {
    var streamId = mapToStreamId.apply(id);
      return get(id).orElseThrow(
      () -> new EntityNotFoundException("Stream with id %s was not found".formatted(streamId))
    );
  }

  private Optional<List<Event>> getEvents(String streamId) {
    ReadResult result;
    try {
      result = eventStore.readStream(streamId, ReadStreamOptions.get()).get();
    } catch (Throwable e) {
      Throwable innerException = e.getCause();

      if (innerException instanceof StreamNotFoundException) {
        return Optional.empty();
      }
      throw new RuntimeException(e);
    }

    var events = result.getEvents().stream()
      .map(EventSerializer::<Event>deserialize)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .toList();

    return Optional.of(events);
  }

  public ETag appendEvents(Entity entity, AppendToStreamOptions appendOptions) {
    var streamId = mapToStreamId.apply(entity.id());
    var events = Arrays.stream(entity.dequeueUncommittedEvents())
      .map(EventSerializer::serialize);

    try {
      var result = eventStore.appendToStream(
        streamId,
        appendOptions,
        events.iterator()
      ).get();

      return toETag(result.getNextExpectedRevision());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  //This ugly hack is needed as ESDB Java client from v4 doesn't allow to access or serialise version in an elegant manner
  private ETag toETag(ExpectedRevision nextExpectedRevision) throws NoSuchFieldException, IllegalAccessException {
    var field = nextExpectedRevision.getClass().getDeclaredField("version");
    field.setAccessible(true);

    return ETag.weak(field.get(nextExpectedRevision));
  }
}
