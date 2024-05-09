package pl.rsww.offerwrite.listeners.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.dominik.api.FlightRequests;

import static pl.rsww.dominik.api.TourOperatorTopics.FLIGHT_INTEGRATION_TOPIC;


@Component
@Slf4j
public class TourOperatorFlightEventListener {

    @KafkaListener(topics = FLIGHT_INTEGRATION_TOPIC, groupId = "OfferWrite", containerFactory = "flightConsumerFactory",autoStartup = "${listen.auto.start:true}")
    public void listenOffer(FlightRequests flightRequests) {
        log.info(String.format("Received update from tour operator %s", flightRequests.getClass()));
    }
}
