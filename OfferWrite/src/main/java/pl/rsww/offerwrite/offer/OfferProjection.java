package pl.rsww.offerwrite.offer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.core.events.EventEnvelope;
import pl.rsww.offerwrite.flights.FlightEvent;
import pl.rsww.offerwrite.hotels.HotelEvent;

@Component
@Slf4j
public class OfferProjection {
    @EventListener
    public void handleHotelCreated(EventEnvelope<HotelEvent.HotelCreated> eventEnvelope) {
        log.info(eventEnvelope.toString());
    }

    @EventListener
    public void handleFlightCreated(EventEnvelope<FlightEvent.FlightCreated> eventEnvelope) {
        log.info(eventEnvelope.toString());
    }
}
