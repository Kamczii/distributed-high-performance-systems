package pl.rsww.order.event.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rsww.order.status.OrderStatus;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderCancelledEvent extends OrderEvent {

    private OrderStatus orderStatus = OrderStatus.CANCELLED;

    public OrderCancelledEvent(UUID orderId, Float totalPrice) {
        super(orderId, totalPrice);
    }
}
