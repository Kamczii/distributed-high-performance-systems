package pl.rsww.offerwrite.core.aggregates;

public interface Aggregate<Id> {
  Id id();

  Object[] dequeueUncommittedEvents();
}
