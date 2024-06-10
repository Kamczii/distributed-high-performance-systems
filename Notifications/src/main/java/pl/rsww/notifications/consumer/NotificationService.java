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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RestController
public class NotificationService {
    private static final int MAX_MESSAGES = 10;
//    private final LinkedList<EventMessage> messageQueue = new LinkedList<>();
    @Autowired
    private DescriptionService descriptionService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = PaymentTopics.PAYMENT_BASIC_TOPIC, groupId = "payment-notification-group", containerFactory = "paymentEventConsumerFactory")
    public void listenToPaymentBasic(PaymentEvent event) {
        log.info("Received payment event: {}", event);
        if (event instanceof PaymentEvent.Pending pending) {
            final var message = descriptionService.describe(pending);
            final var orderTopic = getOrderTopic(pending.orderId());
            send(message, orderTopic);
        }
    }

    @KafkaListener(topics = PaymentTopics.PAYMENT_FAIL_TOPIC, groupId = "payment-notification-group", containerFactory = "paymentEventConsumerFactory")
    public void listenToPaymentFail(PaymentEvent event) {
        log.info("Received payment event: {}", event);
        if (event instanceof PaymentEvent.Fail fail) {
            final var message = descriptionService.describe(fail);
            send(message, getOrderTopic(fail.orderId()));
        }
    }

    @KafkaListener(topics = PaymentTopics.PAYMENT_SUCCESS_TOPIC, groupId = "payment-notification-group", containerFactory = "paymentEventConsumerFactory")
    public void listenToPaymentSuccess(PaymentEvent event) {
        log.info("Received payment event: {}", event);
        if (event instanceof PaymentEvent.Success success) {
            final var message = descriptionService.describe(success);
            final var orderTopic = getOrderTopic(success.orderId());
            send(message, orderTopic);
        }
    }

    @KafkaListener(topics = OfferWriteTopics.OFFER_INTEGRATION_TOPIC, groupId = "notification-group",containerFactory = "offerEventConsumerFactory")
    public void listen(OfferIntegrationEvent event) {
        if (event instanceof OfferIntegrationEvent.Created created) {
            log.info(event.toString());
            EventMessage message = descriptionService.describe(created);
            final var dedicatedTopic = getOfferTopic(created.offerId());
            send(message, dedicatedTopic);
        } else if (event instanceof OfferIntegrationEvent.StatusChanged statusChanged) {
            log.info(event.toString());
            EventMessage message = descriptionService.describe(statusChanged);
            final var dedicatedTopic = getOfferTopic(statusChanged.offerId());
            send(message, dedicatedTopic);
        }
    }

    private void send(EventMessage message, String topic) {
//        synchronized (messageQueue) {
//            if (messageQueue.size() >= MAX_MESSAGES) {
//                messageQueue.pollLast();
//            }
//            messageQueue.addFirst(message);
//        }
        try {
            messagingTemplate.convertAndSend(topic, message);
            log.info("Sending notification to topic \"" + topic + "\"", message);

            messagingTemplate.convertAndSend(getAllTopic(), message);
            log.info("Sending notification to topic \"" + getAllTopic() + "\"", message);
        } catch (Exception e) {
            log.error("Error sending WebSocket message", e);
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
//        synchronized (messageQueue) {
//            return new LinkedList<>(messageQueue);
//        }
        return Collections.emptyList();
    }

    private static String getAllTopic() {
        return "/topic/notifications";
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
