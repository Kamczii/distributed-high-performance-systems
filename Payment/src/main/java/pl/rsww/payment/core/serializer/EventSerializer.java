package pl.rsww.payment.core.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import pl.rsww.payment.api.PaymentEvent;

import java.util.Map;

public class EventSerializer implements Serializer<PaymentEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // no configuration needed
    }

    @Override
    public byte[] serialize(String topic, PaymentEvent paymentEvent) {
        if (paymentEvent == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsBytes(paymentEvent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize OfferCommand", e);
        }
    }

    @Override
    public void close() {
        // no cleanup needed
    }
}