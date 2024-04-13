package pl.rsww.order.event.order;

import pl.rsww.order.status.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderCreatedEvent extends OrderEvent {

    private OrderStatus orderStatus = OrderStatus.CREATED;

    public OrderCreatedEvent(UUID orderId, Float totalPrice) {
        super(orderId, totalPrice);
    }
}
