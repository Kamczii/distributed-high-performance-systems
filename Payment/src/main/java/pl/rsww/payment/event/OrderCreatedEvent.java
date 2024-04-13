package pl.rsww.payment.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rsww.payment.status.OrderStatus;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderCreatedEvent extends OrderEvent {

    private OrderStatus orderStatus = OrderStatus.CREATED;

    public OrderCreatedEvent(UUID orderId, Float totalPrice) {
        super(orderId, totalPrice);
    }
}
