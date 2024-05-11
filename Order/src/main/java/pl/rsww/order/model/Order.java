package pl.rsww.order.model;

import pl.rsww.order.status.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private Long userId;
    private BigDecimal totalPrice;
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

}
