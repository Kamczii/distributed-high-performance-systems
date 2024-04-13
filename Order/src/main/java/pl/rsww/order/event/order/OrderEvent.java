package pl.rsww.order.event.order;

import pl.rsww.order.status.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

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
