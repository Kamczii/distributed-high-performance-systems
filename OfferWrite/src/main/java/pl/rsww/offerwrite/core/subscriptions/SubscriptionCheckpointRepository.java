package pl.rsww.offerwrite.core.subscriptions;

import java.util.Optional;

public interface SubscriptionCheckpointRepository {
  Optional<Long> load(String subscriptionId);

  void store(String subscriptionId, long position);
}
