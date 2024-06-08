package pl.rsww.preference.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.payment.api.PaymentEvent;
import pl.rsww.payment.api.PaymentTopics;
import pl.rsww.preference.service.PreferenceService;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentListener {
    private final PreferenceService preferenceService;

    @KafkaListener(topics = PaymentTopics.PAYMENT_SUCCESS_TOPIC, containerFactory = "paymentSuccessFactory")
    public void listenPaymentSuccess(PaymentEvent paymentEvent) {
        if (Objects.requireNonNull(paymentEvent) instanceof PaymentEvent.Success paymentSuccess) {
            log.info(String.format("Received payment event: %s", paymentEvent));
            preferenceService.addDestination(paymentSuccess.location().country(), paymentSuccess.location().city());
            preferenceService.addHotelAndRoom(paymentSuccess.hotel().name(), paymentSuccess.room().type(), paymentSuccess.room().capacity(), paymentSuccess.room().beds());
        } else {
            log.info(String.format("Ignored payment event: %s", paymentEvent));
        }
    }
}
