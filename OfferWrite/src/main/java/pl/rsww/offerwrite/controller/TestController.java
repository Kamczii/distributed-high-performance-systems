package pl.rsww.offerwrite.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightRepository;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.Hotel;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRepository;
import pl.rsww.offerwrite.offer.OfferService;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    private final OfferRepository offerRepository;
    private final FlightRepository flightRepository;
    private final HotelRepository hotelRepository;
    private final FlightService flightService;
    private final HotelService hotelService;
    private final OfferService offerService;

    @GetMapping("test/flights")
    public ResponseEntity<List<pl.rsww.offerwrite.flights.Flight>> flights() {
        return ResponseEntity.ok(flightRepository.findAll()
                .stream()
                .map(Flight::getFlightId)
                .map(flightService::get)
                .toList());
    }

    @GetMapping("test/hotels")
    public ResponseEntity<List<pl.rsww.offerwrite.hotels.Hotel>> hotels() {
        return ResponseEntity.ok(hotelRepository.findAll()
                .stream()
                .map(Hotel::getId)
                .map(hotelService::getEntity)
                .toList());
    }

    @GetMapping("test/offers")
    public ResponseEntity<List<Map<String, Object>>> offers() {
        return ResponseEntity.ok(offerRepository.findAll()
                .stream()
                .map(this::map)
                .toList());
    }

    @GetMapping("test/offers/{offerId}/lock")
    public ResponseEntity<String> lock(@PathVariable UUID offerId) {
        final var offer = fetchOffer(offerId);

        offerService.reserveOffer(offerId, UUID.randomUUID(), Collections.nCopies(offer.getHotelRoom().getCapacity(), 23));


        return ResponseEntity.ok("Ok");
    }


    @GetMapping("test/offers/locking")
    public ResponseEntity<String> lockall() {

        offerRepository.findAll()
                                       .forEach(offer -> {
                                           try {
                                               offerService.reserveOffer(offer.getId(), UUID.randomUUID(), Collections.nCopies(offer.getHotelRoom().getCapacity(), 23));
                                           } catch (Exception e) {
                                               log.error("Failed for "  +offer.getId());
                                           }
                                       });



        return ResponseEntity.ok("Ok");
    }

    @GetMapping("test/offers/confirm")
    public ResponseEntity<String> confirm() {

        offerRepository.findAll()
                       .stream()
                       .map(offer -> {
                           try {
                               final var orderId = UUID.randomUUID();
                               offerService.reserveOffer(offer.getId(), orderId, Collections.nCopies(offer.getHotelRoom().getCapacity(), 23));
                               return Pair.of(offer.getId(), orderId);
                           } catch (Exception e) {
                               log.error("Failed for "  +offer.getId());
                               return null;
                           }
                       })
                .filter(Objects::nonNull)
                .forEach(pair -> offerService.confirmOffer(pair.getFirst(), pair.getSecond()));



        return ResponseEntity.ok("Ok");
    }


    private Offer fetchOffer(UUID offerId) {
        return offerRepository.findById(offerId)
                              .orElseThrow(() -> new ResourceNotFoundException(Offer.class.toString(), offerId.toString()));
    }

    private Map<String, Object> map(Offer offer) {
        return Map.of(
                "hotel-"+offer.getHotelRoom().getType(), hotelService.getEntity(offer.getHotelRoom().getHotel().getId()),
                "type", offer.getClass()
        );
    }
}
