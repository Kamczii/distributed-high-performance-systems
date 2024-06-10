package pl.rsww.notifications.consumer;

import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;
import pl.rsww.payment.api.PaymentEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DescriptionService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventMessage describe(PaymentEvent.Pending pending) {
        final var payment = new EventMessage("Pending payment", "Pending payment for order %s".formatted(pending.orderId()), getTimestamp());
        payment.getData().put("paymentId", pending.paymentId());
        return payment;
    }

    public EventMessage describe(PaymentEvent.Success success) {
        return new EventMessage("Payment succeed", "Success payment for order %s".formatted(success.orderId()), getTimestamp());
    }

    public EventMessage describe(PaymentEvent.Fail fail) {
        return new EventMessage("Payment failed", "Fail payment for order %s".formatted(fail.orderId()), getTimestamp());
    }

    public EventMessage describe(OfferIntegrationEvent.Created created) {
        return new EventMessage("New offer", "Offer id %s".formatted(created.offerId()), getTimestamp());
    }

    public EventMessage describe(OfferIntegrationEvent.StatusChanged created) {
        return new EventMessage("Status changed", created.offerId().toString() + " updated status to " + created.status(), getTimestamp());
    }

    private static String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }

}
