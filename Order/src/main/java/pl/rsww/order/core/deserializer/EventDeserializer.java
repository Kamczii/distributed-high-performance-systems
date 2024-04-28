package pl.rsww.order.core.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import pl.rsww.order.api.OrderIntegrationEvent;

import java.io.Serializable;
import java.util.Map;

public class EventDeserializer implements Deserializer<Serializable> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do here
    }

    @Override
    public OrderIntegrationEvent deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(data, "UTF-8"), OrderIntegrationEvent.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to OrderIntegrationEvent", e);
        }
    }

    @Override
    public void close() {
        // nothing to do here
    }
}
