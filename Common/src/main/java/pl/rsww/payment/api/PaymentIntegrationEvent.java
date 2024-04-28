package pl.rsww.payment.api;

import java.io.Serializable;
import java.util.UUID;

public record PaymentIntegrationEvent(EventType eventType,
                                      UUID paymentId,
                                      UUID orderId,
                                      String userId,
                                      Float amount
) implements Serializable {
    public enum EventType {
        SUCCEEDED,
        CANCELLED
    }
}
