//package pl.rsww.order.listener;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//import pl.rsww.order.service.OrderService;
//import pl.rsww.order.event.offer.OfferEvent;
//
//@Slf4j
//@Component
//public class OfferListener {
//
//    @Autowired
//    OrderService orderService;
//
//    @KafkaListener(topics = "pl.rsww.offer")
//    public void handleOfferEvent(OfferEvent offerEvent) {
//
//    }
//
//
//
//}
