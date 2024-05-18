package pl.rsww.notifications.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rsww.offerwrite.api.OfferWriteTopics;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@RestController
public class NotificationService {
    private static final int MAX_MESSAGES = 10;
    private final LinkedList<EventMessage> messageQueue = new LinkedList<>();
    @Autowired
    private DescriptionService descriptionService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @KafkaListener(topics = OfferWriteTopics.OFFER_INTEGRATION_TOPIC, groupId = "notification-group",containerFactory = "offerEventConsumerFactory")
    public void listen(OfferIntegrationEvent event) {
        log.info("Listener");
        if (event instanceof OfferIntegrationEvent.Created created) {
            log.info(event.toString());
            EventMessage message = descriptionService.describe(created);

        synchronized (messageQueue) {
            if (messageQueue.size() >= MAX_MESSAGES) {
                messageQueue.pollLast();
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
    } else if (event instanceof OfferIntegrationEvent.StatusChanged statusChanged) {
            log.info(event.toString());
            EventMessage message = descriptionService.describe(statusChanged);

            synchronized (messageQueue) {
                if (messageQueue.size() >= MAX_MESSAGES) {
                    messageQueue.pollLast();
                }
                messageQueue.addFirst(message);
            }
            try {
                messagingTemplate.convertAndSend("/topic/notifications", message);
                messagingTemplate.convertAndSend("/topic/notifications/" +  statusChanged.offerId().toString(), message);
                log.info("Sent offer change to topic \"/topic/notifications\"");
            } catch (Exception e) {
                log.error("Error sending WebSocket message", e);
            }
        }
    }


    @RequestMapping("/initial")
    public List<EventMessage> getInitialMessages() {
        synchronized (messageQueue) {
            return new LinkedList<>(messageQueue);
        }
    }
}
