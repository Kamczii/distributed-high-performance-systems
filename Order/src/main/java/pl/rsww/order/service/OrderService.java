package pl.rsww.order.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import pl.rsww.offerwrite.api.command.OfferCommand;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room;
import pl.rsww.offerwrite.api.response.AvailableLockStatus;
import pl.rsww.order.api.OrderEvent;
import pl.rsww.order.publisher.KafkaPublisher;
import pl.rsww.order.repository.OrderRepository;
import pl.rsww.order.request.OrderRequest;
import pl.rsww.order.status.OrderStatus;
import pl.rsww.order.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_COMMAND_TOPIC;
import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_RESPONSE_TOPIC;
import static pl.rsww.order.api.OrderTopics.ORDER_BASIC_TOPIC;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final KafkaPublisher kafkaPublisher;

    @Transactional
    public UUID createOrder(OrderRequest orderRequest, Long userId) {

        Order order = Order.builder()
                .offerId(orderRequest.getOfferId())
                .userId(userId)
                .orderStatus(OrderStatus.CREATED)
                .orderDate(new Date()).build();

        order = orderRepository.save(order);

        kafkaPublisher.publish(OFFER_COMMAND_TOPIC, new OfferCommand.Lock(order.getOfferId(), order.getOrderId(), orderRequest.getAgeOfVisitors()));
//        kafkaPublisher.publish(ORDER_BASIC_TOPIC, new OrderEvent.Pending(order.getOrderId(), order.getUserId(), BigDecimal.valueOf(333)));
        return order.getOrderId();
    }

    @Transactional
    public void confirmOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        order.setOrderStatus(OrderStatus.ACCEPTED);
        orderRepository.save(order);

    }

    @Transactional
    public void rejectOrder(UUID orderId, Long userId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

    }

    @Transactional
    public void setOrderPrice(UUID orderId, BigDecimal price, AvailableLockStatus lockStatus, Location location, Hotel hotel, Room room) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        order.setTotalPrice(price);

        if (lockStatus == AvailableLockStatus.SUCCESS) order.setOrderStatus(OrderStatus.PENDING);
        else if (lockStatus == AvailableLockStatus.FAIL) order.setOrderStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        if (order.getOrderStatus() == OrderStatus.PENDING)
            kafkaPublisher.publish(ORDER_BASIC_TOPIC, new OrderEvent.Pending(order.getOrderId(), order.getUserId(), order.getTotalPrice(), hotel, room, location));
        if (order.getOrderStatus() == OrderStatus.CANCELLED)
            kafkaPublisher.publish(ORDER_BASIC_TOPIC, new OrderEvent.Cancelled(order.getOrderId()));

    }
}
