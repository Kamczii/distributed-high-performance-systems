package pl.rsww.payment.event;

import pl.rsww.payment.status.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rsww.payment.status.PaymentStatus;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentEvent {
    private UUID paymentId;
    private UUID orderId;
    private Float amount;
    private PaymentStatus paymentStatus;

    public PaymentEvent(UUID paymentId, UUID orderId, Float amount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
    }
}
