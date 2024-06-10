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
            final var message = descriptionService.describe(pending);
            final var orderTopic = getOrderTopic(pending.orderId());
            send(message, orderTopic);
        } else if (event instanceof PaymentEvent.Fail fail) {
            final var message = descriptionService.describe(fail);
            final var orderTopic = getOrderTopic(fail.orderId());
            send(message, orderTopic);
        } else if (event instanceof PaymentEvent.Success success) {
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
            send(message, getAllTopic());
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
        synchronized (messageQueue) {
            if (messageQueue.size() >= MAX_MESSAGES) {
                messageQueue.pollLast();
            }
            messageQueue.addFirst(message);
        }
        try {
            messagingTemplate.convertAndSend(topic, message);
            log.info("Sending notification to topic \"" + topic + "\"", message);
        } catch (Exception e) {
            log.error("Error sending WebSocket message", e);
        }
    }


    @RequestMapping("/initial")
    public List<EventMessage> getInitialMessages() {
        synchronized (messageQueue) {
            return new LinkedList<>(messageQueue);
        }
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

}
