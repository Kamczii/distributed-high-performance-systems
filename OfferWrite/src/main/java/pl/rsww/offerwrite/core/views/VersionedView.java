package pl.rsww.offerwrite.core.views;


import pl.rsww.offerwrite.core.events.EventMetadata;

public interface VersionedView {
  long getLastProcessedPosition();

  void setMetadata(EventMetadata eventMetadata);
}
