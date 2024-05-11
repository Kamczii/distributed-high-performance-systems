package pl.rsww.order.model;

import pl.rsww.order.status.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;
    private UUID offerId;
    private String userId;
    private Float totalPrice;
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

}
