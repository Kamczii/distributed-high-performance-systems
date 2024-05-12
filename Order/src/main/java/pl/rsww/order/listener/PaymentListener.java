package pl.rsww.order.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.OfferWriteTopics;
import pl.rsww.offerwrite.api.command.OfferCommand;
import pl.rsww.offerwrite.api.response.OfferResponse;
import pl.rsww.order.api.OrderEvent;
import pl.rsww.order.service.OrderService;
import pl.rsww.payment.api.PaymentEvent;
import pl.rsww.payment.api.PaymentTopics;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentListener {
    private final OrderService orderService;

    @KafkaListener(topics = PaymentTopics.PAYMENT_SUCCESS_TOPIC, containerFactory = "paymentSuccessFactory")
    public void listenPaymentSuccess(PaymentEvent paymentEvent) {
        if (Objects.requireNonNull(paymentEvent) instanceof PaymentEvent.Success paymentSuccess) {
            log.info(String.format("Received payment event: %s", paymentEvent));
            orderService.confirmOrder(paymentSuccess.orderId());
        } else {
            log.info(String.format("Ignored payment event: %s", paymentEvent));
        }
    }

    @KafkaListener(topics = PaymentTopics.PAYMENT_FAIL_TOPIC, autoStartup = "${listen.auto.start:true}", containerFactory = "paymentFailFactory")
    public void listenPaymentFail(PaymentEvent paymentEvent) {
        if (paymentEvent instanceof PaymentEvent.Fail paymentFail) {
            log.info("Received payment failure event: {}", paymentFail);
            orderService.rejectOrder(paymentFail.orderId(), paymentFail.userId());
        } else {
            log.info("Ignored payment event: {}", paymentEvent);
        }
    }
}
