package pl.rsww.order.api;

import java.util.UUID;

public record OrderIntegrationEvent(EventType eventType,
                                    UUID orderId,
                                    String userId,
                                    Float totalPrice
) {
    public enum EventType {
        CREATED,
        CANCELLED
    }
}
