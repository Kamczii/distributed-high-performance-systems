package pl.rsww.offerwrite.listeners.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.dominik.api.FlightRequests;
import pl.rsww.offerwrite.flights.FlightService;

import static pl.rsww.dominik.api.TourOperatorTopics.FLIGHT_INTEGRATION_TOPIC;


@Slf4j
@Component
@RequiredArgsConstructor
public class TourOperatorFlightEventListener {

    private final FlightService flightService;
    @KafkaListener(topics = FLIGHT_INTEGRATION_TOPIC, groupId = "OfferWrite", containerFactory = "flightConsumerFactory",autoStartup = "${listen.auto.start:true}")
    public void listenOffer(FlightRequests flightRequests) {
        flightService.handle(flightRequests);
    }
}
