package pl.rsww.offerwrite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightRepository;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.Hotel;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRepository;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final OfferRepository offerRepository;
    private final FlightRepository flightRepository;
    private final HotelRepository hotelRepository;
    private final FlightService flightService;
    private final HotelService hotelService;

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

    private Map<String, Object> map(Offer offer) {
        return Map.of(
                "hotel-"+offer.getHotelRoom().getType(), hotelService.getEntity(offer.getHotelRoom().getHotel().getId()),
                "initialFlight", flightService.get(offer.getInitialFlight().getFlightId()),
                "returnFlight", flightService.get(offer.getReturnFlight().getFlightId())
        );
    }
}
