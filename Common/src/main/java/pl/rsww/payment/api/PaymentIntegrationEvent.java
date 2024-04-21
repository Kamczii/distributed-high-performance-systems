package pl.rsww.payment.api;

import java.util.UUID;

public record PaymentIntegrationEvent(EventType eventType,
                                      UUID paymentId,
                                      UUID orderId,
                                      String userId,
                                      Float amount
) {
    public enum EventType {
        SUCCEEDED,
        CANCELLED
    }
}
