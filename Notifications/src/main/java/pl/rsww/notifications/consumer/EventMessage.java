package pl.rsww.notifications.consumer;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
public class EventMessage {
    String title;
    String description;
    String timestamp;
    Map<String, Object> data = new HashMap<>();
}
