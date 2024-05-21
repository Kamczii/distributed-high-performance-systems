package pl.rsww.offerwrite.core.subscriptions;

import java.time.OffsetDateTime;

record CheckpointStored(
  String subscriptionId,
  long position,
  OffsetDateTime checkpointedAt
) {
}
