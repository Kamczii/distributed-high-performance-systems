package pl.rsww.notifications.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;

@Slf4j
@Component
@RestController
public class NotificationService {
    private static final int MAX_MESSAGES = 10;
    private final LinkedList<EventMessage> messageQueue = new LinkedList<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @KafkaListener(topics = "pl.rsww.offerwrite.test", groupId = "notification-group",containerFactory = "offerEventConsumerFactory")
    public void listen(OfferIntegrationEvent event) {
        log.info("Listener");
        if (event instanceof OfferIntegrationEvent.Created created) {
            log.info(event.toString());
                    String timestamp = LocalDateTime.now().format(formatter);  // Formatowanie daty
        EventMessage message = new EventMessage("Created", created.offerId().toString(), timestamp);

        synchronized (messageQueue) {
            if (messageQueue.size() >= MAX_MESSAGES) {
                messageQueue.pollLast(); // Ensure only the latest 10 messages are kept
            }
            messageQueue.addFirst(message);
        }
        try {
            messagingTemplate.convertAndSend("/topic/notifications", message);
            messagingTemplate.convertAndSend("/topic/notifications/" +  created.offerId().toString(), message);
            log.info("Sent offer change to topic \"/topic/notifications\"");
        } catch (Exception e) {
            log.error("Error sending WebSocket message", e);
        }
    }
    }


//    public void listen(Map<String, String> event) {
//        String title = event.get("title");
//        String description = event.get("description");
//        String timestamp = LocalDateTime.now().format(formatter);  // Formatowanie daty
//        EventMessage message = new EventMessage(title, description, timestamp);
//
//        synchronized (messageQueue) {
//            if (messageQueue.size() >= MAX_MESSAGES) {
//                messageQueue.pollLast(); // Ensure only the latest 10 messages are kept
//            }
//            messageQueue.addFirst(message);
//        }
//        log.info("Received event: {} - {}", title, description);
//        try {
//            messagingTemplate.convertAndSend("/topic/notifications", message);
//            messagingTemplate.convertAndSend("/topic/notifications/" + description, message);
//            log.info("Sent offer change to topic \"/topic/notifications\"");
//        } catch (Exception e) {
//            log.error("Error sending WebSocket message", e);
//        }
//    }

    @RequestMapping("/initial")
    public List<EventMessage> getInitialMessages() {
        synchronized (messageQueue) {
            return new LinkedList<>(messageQueue); // Return a copy of the queue
        }
    }
}
