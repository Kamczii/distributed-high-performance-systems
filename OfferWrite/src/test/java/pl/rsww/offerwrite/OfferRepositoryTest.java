package pl.rsww.offerwrite;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.rsww.offerwrite.buses.getting_bus_seats.Bus;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.Hotel;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.offer.getting_offers.BusOffer;
import pl.rsww.offerwrite.offer.getting_offers.FlightOffer;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class OfferRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OfferRepository offerRepository;

    private HotelRoom room1;
    private HotelRoom room2;

    @BeforeEach
    public void setUp() {
        // Sample data setup
        Location location = new Location();
        location.setCity("New York");
        location.setCountry("USA");
        entityManager.persist(location);

        // Creating Flight instances
        Flight initialFlight = new Flight();
        initialFlight.setFlightNumber("FL1234");
        initialFlight.setDeparture(location);
        initialFlight.setDestination(location);
        entityManager.persist(initialFlight);

        Flight returnFlight = new Flight();
        returnFlight.setFlightNumber("FL5678");
        returnFlight.setDeparture(location);
        returnFlight.setDestination(location);
        entityManager.persist(returnFlight);

        // Creating buses
        Bus initBus = new Bus();
        initBus.setBusNumber("FL1234");
        initBus.setDeparture(location);
        initBus.setDestination(location);
        entityManager.persist(initBus);

        Bus returnBus = new Bus();
        returnBus.setBusNumber("FL5678");
        returnBus.setDeparture(location);
        returnBus.setDestination(location);
        entityManager.persist(returnBus);

        // Creating Hotel and HotelRoom instances
        Hotel hotel = new Hotel();
        hotel.setId(UUID.randomUUID());
        hotel.setName("The Grand");
        hotel.setLocation(location);
        entityManager.persist(hotel);

        room1 = createRoom("Double", 4, 2, hotel);
        entityManager.persist(room1);

        room2 = createRoom("Triple", 3, 3, hotel);
        entityManager.persist(room2);

        // Creating FlightOffer and BusOffer instances
        FlightOffer flightOffer = new FlightOffer();
        flightOffer.setHotelRoom(room1);
        flightOffer.setInitialFlight(initialFlight);
        flightOffer.setReturnFlight(returnFlight);
        entityManager.persist(flightOffer);

        BusOffer busOffer = new BusOffer();
        busOffer.setHotelRoom(room2);
        busOffer.setInitialbus(initBus);
        busOffer.setReturnbus(returnBus);
        entityManager.persist(busOffer);
    }

    @Test
    public void testFindAllByHotelRoomIdIn() {
        List<UUID> roomIds = Arrays.asList(room1.getId(), room2.getId());

        List<Offer> offers = offerRepository.findAllByHotelRoomIdIn(roomIds);

        assertThat(offers).hasSize(2);
        assertThat(offers.stream().anyMatch(offer -> offer instanceof FlightOffer)).isTrue();
        assertThat(offers.stream().anyMatch(offer -> offer instanceof BusOffer)).isTrue();
    }

    private static HotelRoom createRoom(String Triple, int capacity, int capacity1, Hotel hotel) {
        HotelRoom room2 = new HotelRoom();
        room2.setType(Triple);
        room2.setCapacity(capacity);
        room2.setBeds(capacity1);
        room2.setHotel(hotel);
        return room2;
    }
}
