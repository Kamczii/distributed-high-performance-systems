package pl.rsww.offerwrite;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.rsww.offerwrite.buses.getting_bus_seats.Bus;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.Hotel;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.offer.getting_offers.BusOffer;
import pl.rsww.offerwrite.offer.getting_offers.FlightOffer;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@DataJpaTest
public class OfferInheritanceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OfferRepository offerRepository;

    @BeforeEach
    public void setUp() {
        offerRepository.deleteAll();

    }

    @Test
    public void testCreateAndFindOffers() {

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

        // Creating HotelRoom instance


        Hotel hotel = new Hotel();
        hotel.setId(UUID.randomUUID());
        hotel.setName("The Grand");
        hotel.setLocation(location);

        entityManager.persist(hotel);

        HotelRoom room1 = createRoom("Double", 4, 2, hotel);
        entityManager.persist(room1);
        HotelRoom room2 = createRoom("Triple", 3, 3, hotel);
        entityManager.persist(room2);

        // Creating FlightOffer instance
        FlightOffer flightOffer = FlightOffer.builder()
                                             .initialFlight(initialFlight)
                                             .returnFlight(returnFlight)
                                             .hotelRoom(room1)
                                             .build();
        entityManager.persist(flightOffer);

        // Creating Bus instances
        Bus initialBus = new Bus();
        initialBus.setBusNumber("BUS1234");
        entityManager.persist(initialBus);

        Bus returnBus = new Bus();
        returnBus.setBusNumber("BUS5678");
        entityManager.persist(returnBus);

        // Creating BusOffer instance
        BusOffer busOffer = BusOffer.builder()
                                    .initialbus(initialBus)
                                    .returnbus(returnBus)
                                    .hotelRoom(room1)
                                    .build();
        entityManager.persist(busOffer);

        entityManager.flush();

        // Retrieving all offers
        List<Offer> offers = offerRepository.findAll();
        assertEquals(2, offers.size());

        // Checking if specific types are present
        assertTrue(offers.stream().anyMatch(o -> o instanceof FlightOffer));
        assertTrue(offers.stream().anyMatch(o -> o instanceof BusOffer));
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
