package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.AvailableOrderStatus;
import pl.rsww.offerwrite.flights.getting_flight_seats.AvailableSeatState;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightRepository;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightSeatRepository;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.Hotel;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoomRepository;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class OfferProjection {
    public static final int MAX_TRIP_DURATION_IN_DAYS = 15;
    public static final int MIN_TRIP_DURATION_IN_DAYS = 1;

    private final HotelRoomRepository hotelRoomRepository;
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final OfferRepository offerRepository;

    @EventListener
    public void handleHotelCreated(Hotel hotel) {
        log.info(String.format("Dodano hotel: %s", hotel.getName()));
        flightRepository.findAllByDestination(hotel.getLocation())
                .forEach(this::handleFlightCreated); //todo: refactor
    }

    @EventListener
    public void handleFlightCreated(Flight flight) {
        log.info(String.format("Dodano lot: %s", flight.getFlightNumber()));
        final var date = flight.getDate();
        final var destination = flight.getDestination();
        final var departure = flight.getDeparture();

        var returnFlights = findFollowingFlights(departure, destination, date)
                .stream()
                .map(returnFlight -> Pair.of(flight, returnFlight))
                .toList();

        var startFlights = findPrecedingFlights(departure, destination, date)
                .stream()
                .map(initialFlight -> Pair.of(initialFlight, flight))
                .toList();

        Stream.concat(returnFlights.stream(), startFlights.stream())
                .forEach(this::createOffers);
    }

    private void createOffers(Pair<Flight,Flight> route) {
        final var initialFlight = route.getFirst();
        final var returnFlight = route.getSecond();
        final var maxCapacity = Math.min(countAvailableSeats(initialFlight), countAvailableSeats(returnFlight));
        final var offers = findRoomsMatchingFlight(initialFlight.getDestination(), maxCapacity, initialFlight.getDate(), returnFlight.getDate())
                .stream()
                .map(room -> Offer.builder()
                        .hotelRoom(room)
                        .initialFlight(initialFlight)
                        .returnFlight(returnFlight)
                        .status(AvailableOrderStatus.OPEN)
                        .build())
                .peek(OfferProjection::log)
                .collect(Collectors.collectingAndThen(Collectors.toList(), offerRepository::saveAll));
        log.info(String.valueOf(offers.size()));
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
        return flightRepository.findByDestinationAndDepartureAndDateBetween(departure, destination, date.plusDays(MIN_TRIP_DURATION_IN_DAYS), date.plusDays(MAX_TRIP_DURATION_IN_DAYS));
    }

    private List<Flight> findPrecedingFlights(Location departure, Location destination, LocalDate date) {
        return flightRepository.findByDestinationAndDepartureAndDateBetween(departure, destination, date.minusDays(MAX_TRIP_DURATION_IN_DAYS), date.minusDays(MIN_TRIP_DURATION_IN_DAYS));
    }
}
