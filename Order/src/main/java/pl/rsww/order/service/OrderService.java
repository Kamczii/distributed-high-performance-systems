package pl.rsww.order.service;

import pl.rsww.order.event.offer.OfferEvent;
import pl.rsww.order.event.order.OrderCancelledEvent;
import pl.rsww.order.publisher.OfferEventPublisher;
import pl.rsww.order.publisher.OrderEventPublisher;
import pl.rsww.order.repository.OrderRepository;
import pl.rsww.order.event.order.OrderCreatedEvent;
import pl.rsww.order.request.OrderRequest;
import pl.rsww.order.status.OrderStatus;
import pl.rsww.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderEventPublisher orderEventPublisher;
    @Autowired
    private OfferEventPublisher offerEventPublisher;

    @Transactional
    public void createOrder(OrderRequest orderRequest) {


        Order order = Order.builder()
                .offerId(orderRequest.getOfferId())
                .totalPrice(orderRequest.getPrice())
                .orderStatus(OrderStatus.CREATED)
                .orderDate(new Date()).build();

        order = orderRepository.save(order);

        orderEventPublisher.publishOrderEvent(new OrderCreatedEvent(
                order.getOrderId(), order.getTotalPrice()
        ));
        offerEventPublisher.publishOfferEvent(new OfferEvent(order.getOfferId()));
    }

    @Transactional
    public void rejectOrder(OrderCancelledEvent orderCancelledEvent) {

        Order order = orderRepository.findById(orderCancelledEvent.getOrderId()).orElse(null);

        assert order != null;
        order.setOrderStatus(orderCancelledEvent.getOrderStatus());

        orderRepository.save(order);

    }
}
