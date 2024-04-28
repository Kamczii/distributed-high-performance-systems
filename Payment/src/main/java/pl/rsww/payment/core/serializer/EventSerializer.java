package pl.rsww.payment.core.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Serializable;
import java.util.Map;

public class EventSerializer implements Serializer<Serializable> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do here
    }

    @Override
    public byte[] serialize(String topic, Serializable data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    @Override
    public void close() {
        // nothing to do here
    }
}