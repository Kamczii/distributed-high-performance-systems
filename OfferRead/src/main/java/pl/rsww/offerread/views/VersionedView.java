package pl.rsww.offerread.views;

import pl.rsww.offerread.events.EventMetadata;

public interface VersionedView {
  long getLastProcessedPosition();

  void setMetadata(EventMetadata eventMetadata);
}
