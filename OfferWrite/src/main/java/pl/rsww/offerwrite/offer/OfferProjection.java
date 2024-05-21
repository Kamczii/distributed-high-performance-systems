package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.buses.BusService;
import pl.rsww.offerwrite.buses.getting_bus_seats.Bus;
import pl.rsww.offerwrite.buses.BusRepository;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.flights.FlightUtils;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightRepository;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.Hotel;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoomRepository;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.offer.getting_offers.BusOffer;
import pl.rsww.offerwrite.offer.getting_offers.FlightOffer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class OfferProjection {
    public static final int MIN_TRIP_DURATION_IN_DAYS = 1;
    public static final int MAX_TRIP_DURATION_IN_DAYS = 15;

    private final HotelRoomRepository hotelRoomRepository;
    private final FlightRepository flightRepository;
    private final OfferRepository offerRepository;
    private final FlightService flightService;
    private final BusRepository busRepository;
    private final BusService busService;

    @EventListener
    public void handleHotelCreated(Hotel hotel) {
        log.info(String.format("Dodano hotel: %s", hotel.getName()));
        flightRepository.findAllByDestination(hotel.getLocation())
                .forEach(this::handleFlightCreated);
    }

    @EventListener
    public void handleFlightCreated(Flight flight) {
        log.info(String.format("Dodano lot: %s", flight.getFlightId()));
        final var date = flight.getDate();
        final var destination = flight.getDestination();
        final var departure = flight.getDeparture();

        var asPrecedingFlight = findFollowingFlights(departure, destination, date)
                .stream()
                .map(returnFlight -> Pair.of(flight, returnFlight))
                .toList();

        var asFollowingFlight = findPrecedingFlights(departure, destination, date)
                .stream()
                .map(initialFlight -> Pair.of(initialFlight, flight))
                .toList();

        Stream.concat(asPrecedingFlight.stream(), asFollowingFlight.stream())
                .forEach(this::createFlightOffers);
    }

    @EventListener
    public void handleBusCreated(Bus bus) {
        log.info(String.format("Dodano bus: %s", bus.getBusId()));
        final var date = bus.getDate();
        final var destination = bus.getDestination();
        final var departure = bus.getDeparture();

        var asPrecedingBus = findFollowingBuses(departure, destination, date)
                .stream()
                .map(returnbus -> Pair.of(bus, returnbus))
                .toList();

        var asFollowingBus = findPrecedingBuses(departure, destination, date)
                .stream()
                .map(initialbus -> Pair.of(initialbus, bus))
                .toList();

        Stream.concat(asPrecedingBus.stream(), asFollowingBus.stream())
              .forEach(this::createBusOffers);
    }



    private void createFlightOffers(Pair<Flight,Flight> route) {
        final var first = route.getFirst();
        final var second = route.getSecond();
        final var initialFlight = getFlightCurrentState(first);
        final var returnFlight = getFlightCurrentState(second);
        final var maxCapacity = Math.min(initialFlight.getCapacity(), returnFlight.getCapacity());
        final var offers = findRoomsMatchingFlight(first.getDestination(), maxCapacity, initialFlight.getDate(), returnFlight.getDate())
                .stream()
                .map(room -> FlightOffer.builder()
                                        .hotelRoom(room)
                                        .initialFlight(first)
                                        .returnFlight(second)
                                        .build())
                .peek(OfferProjection::log)
                .collect(Collectors.collectingAndThen(Collectors.toList(), offerRepository::saveAll));
    }

    private void createBusOffers(Pair<Bus,Bus> route) {
        final var first = route.getFirst();
        final var second = route.getSecond();
        final var initialFlight = getBusCurrentState(first.getBusId());
        final var returnFlight = getBusCurrentState(second.getBusId());
        final var maxCapacity = Math.min(initialFlight.getCapacity(), returnFlight.getCapacity());
        final var offers = findRoomsMatchingFlight(first.getDestination(), maxCapacity, initialFlight.getDate(), returnFlight.getDate())
                .stream()
                .map(room -> BusOffer.builder()
                                     .hotelRoom(room)
                                     .initialbus(first)
                                     .returnbus(second)
                                     .build())
                .peek(OfferProjection::log)
                .collect(Collectors.collectingAndThen(Collectors.toList(), offerRepository::saveAll));
    }

    private pl.rsww.offerwrite.buses.Bus getBusCurrentState(String id) {
        return busService.get(id);
    }

    private pl.rsww.offerwrite.flights.Flight getFlightCurrentState(Flight flight) {
        return flightService.get(FlightUtils.flightId(flight.getFlightNumber(), flight.getDate()));
    }

    private static void log(FlightOffer offer) {
        log.info(String.format("Oferta: Od %s do %s, z %s do %s dla %d osób. Pokój: %s, %d łóżek",
                offer.getInitialFlight().getDate(), offer.getReturnFlight().getDate(), offer.getInitialFlight().getDeparture(), offer.getReturnFlight().getDeparture(), offer.getHotelRoom().getCapacity(), offer.getHotelRoom().getType(), offer.getHotelRoom().getBeds()));
    }

    private static void log(BusOffer offer) {
        log.info(String.format("Oferta: Od %s do %s, z %s do %s dla %d osób. Pokój: %s, %d łóżek",
                offer.getInitialbus().getDate(), offer.getReturnbus().getDate(), offer.getInitialbus().getDeparture(), offer.getReturnbus().getDeparture(), offer.getHotelRoom().getCapacity(), offer.getHotelRoom().getType(), offer.getHotelRoom().getBeds()));
    }
    private List<HotelRoom> findRoomsMatchingFlight(Location location, int maxCapacity, LocalDate checkIn, LocalDate checkout) {
        return hotelRoomRepository.find(location, maxCapacity, checkIn, checkout);
    }


    private List<Flight> findFollowingFlights(Location departure, Location destination, LocalDate date) {
        return flightRepository.findByDestinationAndDepartureAndDateBetween(departure, destination, date.plusDays(MIN_TRIP_DURATION_IN_DAYS), date.plusDays(MAX_TRIP_DURATION_IN_DAYS));
    }

    private List<Flight> findPrecedingFlights(Location departure, Location destination, LocalDate date) {
        return flightRepository.findByDestinationAndDepartureAndDateBetween(departure, destination, date.minusDays(MAX_TRIP_DURATION_IN_DAYS), date.minusDays(MIN_TRIP_DURATION_IN_DAYS));
    }

    private List<Bus> findFollowingBuses(Location departure, Location destination, LocalDate date) {
        return busRepository.findByDestinationAndDepartureAndDateBetween(departure, destination, date.plusDays(MIN_TRIP_DURATION_IN_DAYS), date.plusDays(MAX_TRIP_DURATION_IN_DAYS));
    }

    private List<Bus> findPrecedingBuses(Location departure, Location destination, LocalDate date) {
        return busRepository.findByDestinationAndDepartureAndDateBetween(departure, destination, date.minusDays(MAX_TRIP_DURATION_IN_DAYS), date.minusDays(MIN_TRIP_DURATION_IN_DAYS));
    }
}
