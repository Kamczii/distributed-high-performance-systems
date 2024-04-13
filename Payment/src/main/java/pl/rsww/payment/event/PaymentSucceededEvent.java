package pl.rsww.payment.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rsww.payment.status.PaymentStatus;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentSucceededEvent extends PaymentEvent {

    private PaymentStatus paymentStatus = PaymentStatus.SUCCESS;

    public PaymentSucceededEvent(UUID paymentId, UUID orderId, Float amount) {
        super(paymentId, orderId, amount);
    }
}
