package pl.rsww.payment.service;

import pl.rsww.payment.event.OrderCancelledEvent;
import pl.rsww.payment.event.OrderCreatedEvent;
import pl.rsww.payment.event.PaymentCancelledEvent;
import pl.rsww.payment.event.PaymentSucceededEvent;
import pl.rsww.payment.model.Payment;
import pl.rsww.payment.publisher.OrderEventPublisher;
import pl.rsww.payment.publisher.PaymentEventPublisher;
import pl.rsww.payment.repository.PaymentRepository;
import pl.rsww.payment.status.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

@Service
public class PaymentService {

    private static final Random random = new Random();
    private static final int SIMULATION_SUCCESS_RATE = 80;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderEventPublisher orderEventPublisher;
    @Autowired
    private PaymentEventPublisher paymentEventPublisher;

    @Transactional
    public void createPayment(OrderCreatedEvent orderEvent) {

        Payment payment = Payment.builder()
                .orderId(orderEvent.getOrderId())
                .amount(orderEvent.getTotalPrice())
                .createdAt(new Date())
                .paymentStatus(PaymentStatus.PENDING).build();

        payment = paymentRepository.save(payment);

        processPayment(payment);

    }

    public void processPayment(Payment payment) {
        if (random.nextInt(100) < SIMULATION_SUCCESS_RATE) {
            updatePaymentStatus(payment, PaymentStatus.SUCCESS);
            sendPaymentSuccessEvent(payment);
        } else {
            updatePaymentStatus(payment, PaymentStatus.CANCELLED);
            sendPaymentCancelEvent(payment);
            sendOrderCancelEvent(payment);
        }
    }

    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus) {
        Payment paymentToUpdate = paymentRepository.findById(payment.getId()).orElse(null);

        assert paymentToUpdate != null;
        paymentToUpdate.setPaymentStatus(paymentStatus);
        paymentRepository.save(paymentToUpdate);
    }

    public void sendPaymentSuccessEvent(Payment payment) {
        PaymentSucceededEvent paymentSucceededEvent = new PaymentSucceededEvent(payment.getId(),
                payment.getOrderId(),
                payment.getAmount());
        paymentEventPublisher.publishPaymentEvent(paymentSucceededEvent);
    }

    public void sendPaymentCancelEvent(Payment payment) {
        PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent(payment.getId(),
                payment.getOrderId(),
                payment.getAmount());
        paymentEventPublisher.publishPaymentEvent(paymentCancelledEvent);
    }

    public void sendOrderCancelEvent(Payment payment) {
        OrderCancelledEvent orderCancelEvent = new OrderCancelledEvent(payment.getOrderId(),
                payment.getAmount());
        orderEventPublisher.publishOrderEvent(orderCancelEvent);
    }
}
