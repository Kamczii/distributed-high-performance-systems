package pl.rsww.offerwrite.listeners.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.dominik.api.FlightRequests;
import pl.rsww.dominik.api.HotelRequests;

import static pl.rsww.dominik.api.TourOperatorTopics.FLIGHT_INTEGRATION_TOPIC;
import static pl.rsww.dominik.api.TourOperatorTopics.HOTEL_INTEGRATION_TOPIC;
import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_INTEGRATION_TOPIC;

@Component
@Slf4j
public class HotelEventListener {

    @KafkaListener(topics = HOTEL_INTEGRATION_TOPIC, groupId = "OfferWrite", containerFactory = "hotelConsumerFactory",autoStartup = "${listen.auto.start:true}")
    public void listenOffer(HotelRequests hotelRequests) {
        log.info(String.format("Received update from tour operator %s", hotelRequests.getClass()));
    }
}
