package pl.rsww.payment.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rsww.payment.status.OrderStatus;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderEvent {
    protected UUID orderId;
    protected Float totalPrice;
    protected OrderStatus orderStatus;

    public OrderEvent(UUID orderId, Float totalPrice) {
        this.totalPrice = totalPrice;
        this.orderId = orderId;
    }
}
