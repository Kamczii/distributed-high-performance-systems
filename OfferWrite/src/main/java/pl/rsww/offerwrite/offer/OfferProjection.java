package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.core.events.EventEnvelope;
import pl.rsww.offerwrite.flights.FlightEvent;
import pl.rsww.offerwrite.flights.getting_flight_seats.AvailableSeatState;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightRepository;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightSeatRepository;
import pl.rsww.offerwrite.hotels.HotelEvent;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoomRepository;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.location.LocationRepository;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class OfferProjection {
    public static final int MAX_TRIP_DURATION = 15;
    public static final int MIN_TRIP_DURATION = 1;

    private final HotelRoomRepository hotelRoomRepository;
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final LocationRepository locationRepository;
    private final OfferRepository offerRepository;

    //todo przenieść do entity listenera
    @EventListener
    public void handleHotelCreated(EventEnvelope<HotelEvent.HotelCreated> eventEnvelope) {
        log.info(eventEnvelope.data().toString());
        //todo
    }

    @EventListener
    public void handleFlightCreated(EventEnvelope<FlightEvent.FlightCreated> eventEnvelope) {
        log.info(eventEnvelope.data().toString());
        final var data = eventEnvelope.data();
        final var date = data.date();
        final var destination = getLocation(data.destination());
        final var departure = getLocation(data.departure());
        final var flight = fetchFlight(data);

        var returnFlights = findFollowingFlights(departure, destination, date)
                .stream()
                .map(returnFlight -> Pair.of(flight, returnFlight))
                .toList();

        var startFlights = findPrecedingFlights(departure, destination, date)
                .stream()
                .map(initialFlight -> Pair.of(initialFlight, flight))
                .toList();

        Stream.concat(returnFlights.stream(), startFlights.stream())
                .forEach(this::createOffer);
    }

    private Flight fetchFlight(FlightEvent.FlightCreated data) {
        return flightRepository.findByFlightNumberAndDate(data.flightNumber(), data.date());
    }

    private void createOffer(Pair<Flight,Flight> route) {
        var initialFlight = route.getFirst();
        var returnFlight = route.getSecond();
        var maxCapacity = Math.min(countAvailableSeats(initialFlight), countAvailableSeats(returnFlight));
        findRoomsMatchingFlight(initialFlight.getDestination(), maxCapacity, initialFlight.getDate(), returnFlight.getDate())
                .stream()
                .map(room -> Offer.builder()
                        .hotelRoom(room)
                        .initialFlight(initialFlight)
                        .returnFlight(returnFlight)
                        .build())
                .peek(OfferProjection::log)
                .collect(Collectors.collectingAndThen(Collectors.toList(), offerRepository::saveAll));
    }

    private static void log(Offer offer) {
        log.info(String.format("Oferta: Od %s do %s, z %s do %s dla %d osób. Pokój: %s, %d łóżek",
                offer.getInitialFlight().getDate(), offer.getReturnFlight().getDate(), offer.getInitialFlight().getDeparture(), offer.getReturnFlight().getDeparture(), offer.getHotelRoom().getCapacity(), offer.getHotelRoom().getType(), offer.getHotelRoom().getBeds()));
    }

    private List<HotelRoom> findRoomsMatchingFlight(Location location, int maxCapacity, LocalDate checkIn, LocalDate checkout) {
        return hotelRoomRepository.find(location, maxCapacity, checkIn, checkout);
    }

    private int countAvailableSeats(Flight departure) {
        return flightSeatRepository.countByFlightIdAndSeatStateState(departure.getId(), AvailableSeatState.OPEN);
    }


    private List<Flight> findFollowingFlights(Location departure, Location destination, LocalDate date) {
        return flightRepository.findByDestinationAndDepartureAndDateBetween(departure, destination, date.plusDays(MIN_TRIP_DURATION), date.plusDays(MAX_TRIP_DURATION));
    }

    private List<Flight> findPrecedingFlights(Location departure, Location destination, LocalDate date) {
        return flightRepository.findByDestinationAndDepartureAndDateBetween(departure, destination, date.minusDays(MAX_TRIP_DURATION), date.minusDays(MIN_TRIP_DURATION));
    }

    private Location getLocation(pl.rsww.offerwrite.common.location.Location destination) {
        return locationRepository.findByCityAndCountry(destination.city(), destination.country()).orElseThrow();
    }
}
