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
import pl.rsww.offerwrite.producer.ObjectRequestKafkaProducer;

import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;
import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_INTEGRATION_TOPIC;

@Slf4j

@Service
@RequiredArgsConstructor
public class OfferStatusService {

    private final FlightService flightService;
    private final OfferRepository offerRepository;
    private final HotelService hotelService;
    private final ObjectRequestKafkaProducer objectRequestKafkaProducer;


    public void updateStatus(UUID offerId) {
        final var offer = fetchOffer(offerId);
        final var initialFlight = getFlightCurrentState(offer.getInitialFlight());
        final var returnFlight = getFlightCurrentState(offer.getReturnFlight());
        final var hotelRoom = offer.getHotelRoom();
        final var hotel = getHotelCurrentState(hotelRoom.getHotel().getId());

        final var offerAvailable = Stream.of(
                hotel.roomAvailable(hotelRoom.getType(), offer.getInitialFlight().getDate(), offer.getReturnFlight().getDate()),
                initialFlight.seatsAvailable(hotelRoom.getCapacity()),
                returnFlight.seatsAvailable(hotelRoom.getCapacity())
        ).allMatch(TRUE::equals);

        if (offerAvailable) {
            log.info("Unlocking " + offerId);
            final var message = map(offer, AvailableOfferStatus.OPEN);
            publish(message);
        }
    }

    private String flightId(Flight flight) {
        return FlightUtils.flightId(flight.getFlightNumber(), flight.getDate());
    }

    private pl.rsww.offerwrite.flights.Flight getFlightCurrentState(Flight flight) {
        return flightService.get(flightId(flight));
    }
    private Hotel getHotelCurrentState(UUID hotelId) {
        return hotelService.getEntity(hotelId);
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
