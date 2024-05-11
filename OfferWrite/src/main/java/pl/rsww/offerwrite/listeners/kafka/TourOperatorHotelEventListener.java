package pl.rsww.offerwrite.listeners.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.dominik.api.HotelRequests;
import pl.rsww.offerwrite.hotels.HotelService;

import static pl.rsww.dominik.api.TourOperatorTopics.HOTEL_INTEGRATION_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
public class TourOperatorHotelEventListener {

    private final HotelService hotelService;

    @KafkaListener(topics = HOTEL_INTEGRATION_TOPIC, groupId = "OfferWrite", containerFactory = "hotelConsumerFactory",autoStartup = "${listen.auto.start:true}")
    public void listenOffer(HotelRequests hotelRequests) {
        hotelService.handle(hotelRequests);
    }
}
