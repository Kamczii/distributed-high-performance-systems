package pl.rsww.payment.model;

import pl.rsww.payment.status.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID orderId;
    private String userId;
    private Float amount;
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
