package pl.rsww.notifications.consumer;

import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;
import pl.rsww.payment.api.PaymentEvent;
import pl.rsww.preference.api.PreferenceEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DescriptionService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventMessage describe(PaymentEvent.Pending pending) {
        final var payment = new EventMessage("Payment", pending.paymentId().toString(), getTimestamp());
        payment.getData().put("paymentId", pending.paymentId());
        return payment;
    }

    public EventMessage describe(OfferIntegrationEvent.Created created) {
        return new EventMessage("Created", created.offerId().toString(), getTimestamp());
    }

    public EventMessage describe(OfferIntegrationEvent.StatusChanged created) {
        return new EventMessage("Status changed", created.offerId().toString() + " updated status to " + created.status(), getTimestamp());
    }

    public EventMessage describe(PreferenceEvent.Hotel hotel) {
        return new EventMessage("Hotel Preference", hotel.name() + ", " + hotel.room().type() + " Room, " + hotel.room().capacity() + " Person", getTimestamp());
    }

    public EventMessage describe(PreferenceEvent.Destination destination) {
        return new EventMessage("Destination Preference", destination.city() + ", " + destination.country(), getTimestamp());
    }

    private static String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }

}
