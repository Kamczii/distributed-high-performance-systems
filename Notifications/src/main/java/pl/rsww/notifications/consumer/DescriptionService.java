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
        return new EventMessage("Payment", pending.paymentId().toString(), getTimestamp());
    }

    public EventMessage describe(OfferIntegrationEvent.Created created) {
        return new EventMessage("Created", created.offerId().toString(), getTimestamp());
    }

    public EventMessage describe(OfferIntegrationEvent.StatusChanged created) {
        return new EventMessage("Status changed", created.offerId().toString() + " changed status to " + created.status(), getTimestamp());
    }

    private static String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }

}
