package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.core.events.EventEnvelope;
import pl.rsww.offerwrite.flights.FlightEvent;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightRepository;
import pl.rsww.offerwrite.hotels.HotelEvent;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRepository;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoomRepository;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.location.LocationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OfferProjection {
    private final HotelRepository hotelRepository;
    private final HotelRoomRepository hotelRoomRepository;
    private final FlightRepository flightRepository;
    private final LocationRepository locationRepository;
    //todo przenieść do entity listenera
    @EventListener
    public void handleHotelCreated(EventEnvelope<HotelEvent.HotelCreated> eventEnvelope) {
        log.info(eventEnvelope.data().toString());
    }

    @EventListener
    public void handleFlightCreated(EventEnvelope<FlightEvent.FlightCreated> eventEnvelope) {
        log.info(eventEnvelope.data().toString());
        var data = eventEnvelope.data();
        LocalDate date = data.date();
        var destination = getLocation(data.destination());
        var departure = getLocation(data.departure());
        var returnFlights = flightRepository.findByDestinationAndDepartureAndDateBetween(departure, destination, date.plusDays(1), date.plusDays(15));
        var startFlights = flightRepository.findByDestinationAndDepartureAndDateBetween(departure, destination, date.minusDays(15), date.minusDays(1));

        for (var returnFlight : returnFlights) {
            LocalDate returnDate = returnFlight.getDate();
            List<HotelRoom> rooms = hotelRoomRepository.find(destination, data.capacity(), date, returnDate);

            for (HotelRoom room : rooms) {
                log.info(String.format("Oferta: Od %s do %s, z %s do %s dla %d osób. Pokój: %s, %d łóżek",
                        date, returnDate, departure.toString(), destination.toString(), room.getCapacity(), room.getType(), room.getBeds()));
            }
        }

        for (var startFlight : startFlights) {
            LocalDate startDate = startFlight.getDate();
            List<HotelRoom> rooms = hotelRoomRepository.find(destination, data.capacity(), startDate, date);

            for (HotelRoom room : rooms) {
                log.info(String.format("Oferta: Od %s do %s, z %s do %s dla %d osób. Pokój: %s, %d łóżek",
                        startDate, date, departure.toString(), destination.toString(), room.getCapacity(), room.getType(), room.getBeds()));
            }
        }
    }

    private Location getLocation(pl.rsww.offerwrite.common.location.Location destination) {
        return locationRepository.findByCityAndCountry(destination.city(), destination.country()).orElseThrow();
    }
}
