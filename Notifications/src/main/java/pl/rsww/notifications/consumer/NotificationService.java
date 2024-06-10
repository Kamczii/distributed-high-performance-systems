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
import pl.rsww.payment.api.PaymentEvent;
import pl.rsww.payment.api.PaymentTopics;
import pl.rsww.preference.api.PreferenceEvent;
import pl.rsww.preference.api.PreferenceTopics;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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

    @KafkaListener(topics = PaymentTopics.PAYMENT_BASIC_TOPIC, groupId = "payment-notification-group")
    public void listenToPaymentBasic(PaymentEvent event) {
        if (event instanceof PaymentEvent.Pending pending) {
            log.info(event.toString());
            EventMessage message = descriptionService.describe(pending);

            synchronized (messageQueue) {
                if (messageQueue.size() >= MAX_MESSAGES) {
                    messageQueue.pollLast();
                }
                messageQueue.addFirst(message);
            }
            try {
                final var orderTopic = getOrderTopic(pending.orderId());
                messagingTemplate.convertAndSend(orderTopic, message);
                log.info("Sent payment notification change to topic \"" + orderTopic + "\"");
            } catch (Exception e) {
                log.error("Error sending WebSocket message", e);
            }
        }
    }
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
            final var dedicatedTopic = getOfferTopic(created.offerId());
            messagingTemplate.convertAndSend(dedicatedTopic, message);
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
                final var dedicatedTopic = getOfferTopic(statusChanged.offerId());
                messagingTemplate.convertAndSend(dedicatedTopic, message);
                log.info("Sent offer change to topic \"" + dedicatedTopic + "\"");
            } catch (Exception e) {
                log.error("Error sending WebSocket message", e);
            }
        }
    }

    @KafkaListener(topics = PreferenceTopics.PREFERENCE_BASIC_TOPIC, groupId = "preference-group")
    public void listenPreference(PreferenceEvent event) {
        log.info("PREFERENCE LISTENER");
        if (event instanceof PreferenceEvent.Hotel hotel) {
            log.info(event.toString());
            EventMessage message = descriptionService.describe(hotel);

//        synchronized (messageQueue) {
//            if (messageQueue.size() >= MAX_MESSAGES) {
//                messageQueue.pollLast();
//            }
//            messageQueue.addFirst(message);
//        }
        try {
            messagingTemplate.convertAndSend("/topic/preferences/hotels", message);
            final var dedicatedTopic = getPreferenceHotelTopic();
            messagingTemplate.convertAndSend(dedicatedTopic, message);
            log.info("Sent hotel preference to topic \"/topic/preferences/hotels\"");
        } catch (Exception e) {
            log.error("Error sending WebSocket message", e);
        }
    } else if (event instanceof PreferenceEvent.Destination destination) {
            log.info(event.toString());
            EventMessage message = descriptionService.describe(destination);

//            synchronized (messageQueue) {
//                if (messageQueue.size() >= MAX_MESSAGES) {
//                    messageQueue.pollLast();
//                }
//                messageQueue.addFirst(message);
//            }
            try {
                messagingTemplate.convertAndSend("/topic/preferences/destinations", message);
                final var dedicatedTopic = getPreferenceDestinationTopic();
                messagingTemplate.convertAndSend(dedicatedTopic, message);
                log.info("Sent destination preference to topic \"" + dedicatedTopic + "\"");
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

    private String getOfferTopic(UUID offerId) {
        return "/topic/offers/" + offerId;
    }

    private String getPaymentTopic(UUID paymentId) {
        return "/topic/payments/" + paymentId;
    }

    private String getOrderTopic(UUID orderId) {
        return "/topic/orders/" + orderId;
    }

    private String getPreferenceHotelTopic() {
        return "/topic/preferences/hotels";
    }

    private String getPreferenceDestinationTopic() {
        return "/topic/preferences/destinations";
    }

}
