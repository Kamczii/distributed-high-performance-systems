package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.api.integration.AvailableOfferStatus;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.flights.FlightUtils;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.hotels.Hotel;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;
import pl.rsww.offerwrite.offer.getting_offers.strategy.context.impl.OfferStrategyContextImpl;
import pl.rsww.offerwrite.producer.ObjectRequestKafkaProducer;

import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;
import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_INTEGRATION_TOPIC;

@Slf4j

@Service
@RequiredArgsConstructor
public class OfferStatusService {

    private final OfferRepository offerRepository;
    private final ObjectRequestKafkaProducer objectRequestKafkaProducer;
    private final OfferStrategyContextImpl offerStrategyContext;


    public void updateStatus(UUID offerId) {
        final var offer = fetchOffer(offerId);
        final var offerAvailable = offerStrategyContext.resolve(offer)
                .isOfferAvailable(offer);

        if (offerAvailable) {
            log.info("Unlocking " + offer.getId());
            final var message = map(offer, AvailableOfferStatus.OPEN);
            publish(message);
        }
    }

    private Offer fetchOffer(UUID offerId) {
        return offerRepository.findById(offerId)
                              .orElseThrow(() -> new ResourceNotFoundException(Offer.class.toString(), offerId.toString()));
    }

    private pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.StatusChanged map(Offer offer, AvailableOfferStatus status) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.StatusChanged(offer.getId(), status);
    }
    private void publish(pl.rsww.offerwrite.api.integration.OfferIntegrationEvent event) {
        objectRequestKafkaProducer.produce(OFFER_INTEGRATION_TOPIC, event);
    }
}
