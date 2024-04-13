package pl.rsww.payment.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rsww.payment.status.OrderStatus;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderCancelledEvent extends OrderEvent {

    private OrderStatus orderStatus = OrderStatus.CANCELLED;

    public OrderCancelledEvent(UUID orderId, Float totalPrice) {
        super(orderId, totalPrice);
    }
}
