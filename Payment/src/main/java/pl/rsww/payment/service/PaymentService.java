package pl.rsww.payment.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room;
import pl.rsww.payment.api.PaymentEvent;
import pl.rsww.payment.model.Payment;
import pl.rsww.payment.publisher.KafkaPublisher;
import pl.rsww.payment.repository.PaymentRepository;
import pl.rsww.payment.request.PaymentRequest;
import pl.rsww.payment.status.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static pl.rsww.payment.api.PaymentTopics.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Random random = new Random();
    private static final int SIMULATION_SUCCESS_RATE = 100;

    private final PaymentRepository paymentRepository;
    private final KafkaPublisher kafkaPublisher;
    private final ConcurrentMap<UUID, PaymentTempDetails> paymentTempDetails = new ConcurrentHashMap<>();

    @Transactional
    public void createPayment(UUID orderId, Long userId, BigDecimal amount, Location location, Hotel hotel, Room room) {

        Payment payment = Payment.builder()
                .orderId(orderId)
                .userId(userId)
                .amount(amount)
                .createdAt(new Date())
                .paymentStatus(PaymentStatus.PENDING).build();

        payment = paymentRepository.save(payment);

        paymentTempDetails.put(payment.getId(), new PaymentTempDetails(location, hotel, room));

        kafkaPublisher.publish(PAYMENT_BASIC_TOPIC, new PaymentEvent.Pending(payment.getId(),payment.getOrderId(), payment.getUserId(), payment.getAmount()));
//        processPayment(payment);

    }

    public Boolean processPayment(Payment payment) {
        if (random.nextInt(100) < SIMULATION_SUCCESS_RATE) {
            updatePaymentStatus(payment, PaymentStatus.SUCCESS);
            PaymentTempDetails details = paymentTempDetails.get(payment.getId());
            kafkaPublisher.publish(PAYMENT_SUCCESS_TOPIC, new PaymentEvent.Success(payment.getOrderId(), payment.getUserId(), details.getHotel(), details.getRoom(), details.getLocation()));
            return true;
        } else {
            updatePaymentStatus(payment, PaymentStatus.FAIL);
            kafkaPublisher.publish(PAYMENT_FAIL_TOPIC, new PaymentEvent.Fail(payment.getOrderId(), payment.getUserId()));
            return false;
        }
    }

    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus) {
        Payment paymentToUpdate = paymentRepository.findById(payment.getId()).orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + payment.getId()));

        paymentToUpdate.setPaymentStatus(paymentStatus);
        paymentRepository.save(paymentToUpdate);
    }

    public Boolean acceptPayment(PaymentRequest paymentRequest, Long userId) {
        Payment payment = paymentRepository.findById(paymentRequest.getPaymentId()).orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentRequest.getPaymentId()));
        return processPayment(payment);
    }

    public void cancelPayment(PaymentRequest paymentRequest, Long userId) {
        Payment payment = paymentRepository.findById(paymentRequest.getPaymentId()).orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentRequest.getPaymentId()));
        updatePaymentStatus(payment, PaymentStatus.FAIL);
        kafkaPublisher.publish(PAYMENT_FAIL_TOPIC, new PaymentEvent.Fail(payment.getOrderId(), payment.getUserId()));
    }

    @Getter
    @AllArgsConstructor
    private static class PaymentTempDetails {
        private final Location location;
        private final Hotel hotel;
        private final Room room;
    }

}
