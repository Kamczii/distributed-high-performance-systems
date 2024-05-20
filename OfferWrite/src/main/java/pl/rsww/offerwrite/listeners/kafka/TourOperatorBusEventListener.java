package pl.rsww.offerwrite.listeners.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.buses.BusService;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.tour_operator.api.BusRequests;
import pl.rsww.tour_operator.api.FlightRequests;

import static pl.rsww.tour_operator.api.TourOperatorTopics.BUS_INTEGRATION_TOPIC;
import static pl.rsww.tour_operator.api.TourOperatorTopics.FLIGHT_INTEGRATION_TOPIC;


@Slf4j
@Component
@RequiredArgsConstructor
public class TourOperatorBusEventListener {

    private final BusService busService;

    @KafkaListener(topics = BUS_INTEGRATION_TOPIC, groupId = "OfferWrite", containerFactory = "busConsumerFactory",autoStartup = "${listen.auto.start:true}")
    public void listenOffer(BusRequests busRequests) {
        busService.handle(busRequests);
    }
}
