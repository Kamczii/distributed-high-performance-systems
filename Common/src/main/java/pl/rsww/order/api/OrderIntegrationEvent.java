package pl.rsww.order.api;

import java.io.Serializable;
import java.util.UUID;

public record OrderIntegrationEvent(EventType eventType,
                                    UUID orderId,
                                    String userId,
                                    Float totalPrice
) implements Serializable {
    public enum EventType {
        CREATED,
        CANCELLED
    }
}
