package pl.rsww.offerwrite.api.backgroundworkers;

import com.eventstore.dbclient.EventStoreDBClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.core.events.EventBus;
import pl.rsww.offerwrite.core.subscriptions.EventStoreDBSubscriptionToAll;
import pl.rsww.offerwrite.core.subscriptions.SubscriptionCheckpointRepository;

@Component
@RequiredArgsConstructor
public class EventStoreDBSubscriptionBackgroundWorker implements SmartLifecycle {
  private final SubscriptionCheckpointRepository subscriptionCheckpointRepository;
  private final EventStoreDBClient eventStore;
  private final EventBus eventBus;
  private EventStoreDBSubscriptionToAll subscription;
  private final Logger logger = LoggerFactory.getLogger(EventStoreDBSubscriptionBackgroundWorker.class);



  @Override
  public void start() {
    try {
      subscription = new EventStoreDBSubscriptionToAll(
        eventStore,
        subscriptionCheckpointRepository,
        eventBus
      );
      subscription.subscribeToAll();
    } catch (Throwable e) {
      logger.error("Failed to start Subscription to All", e);
    }
  }

  @Override
  public void stop() {
    stop(() -> {});
  }

  @Override
  public boolean isRunning() {
    return subscription != null && subscription.isRunning();
  }

  @Override
  public int getPhase() {
    return Integer.MAX_VALUE;
  }

  @Override
  public boolean isAutoStartup() {
    return true;
  }

  @Override
  public void stop(Runnable callback) {
    if (!isRunning()) return;

    subscription.stop();

    callback.run();
  }
}
