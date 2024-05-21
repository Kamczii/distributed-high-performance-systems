package pl.rsww.offerwrite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.*;
import pl.rsww.offerwrite.location.Location;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;



@DataJpaTest
public class HotelRoomRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    HotelRoomRepository hotelRoomRepository;

    @Autowired
    HotelRepository hotelRepository;
    private Location location;

    @BeforeEach
    public void setup() {
        Location location = new Location();
        location.setCity("New York");
        location.setCountry("USA");
        entityManager.persist(location);

        Hotel hotel = new Hotel();
        hotel.setName("The Grand");
        hotel.setLocation(location);

        entityManager.persist(hotel);

        HotelRoom room1 = createRoom("Double", 4, 2, hotel);
        entityManager.persist(room1);
        HotelRoom room2 = createRoom("Triple", 3, 3, hotel);
        entityManager.persist(room2);



        // Create booking data
        createBooking(1, 10, room1);
        createBooking(15, 20, room1);
        createBooking(28, 30, room1);

        createBooking(8, 13, room2);
        createBooking(16, 18, room2);
        createBooking(21, 30, room2);

        entityManager.flush();

        this.location = location;
    }

    private void createBooking(int checkIn, int checkOut, HotelRoom room1) {
        Booking booking2 = new Booking();
        booking2.setCheckIn(getDayOfOctober(checkIn));
        booking2.setCheckOut(getDayOfOctober(checkOut));
        booking2.setRoom(room1);
        booking2.setConfirmed(true);
        entityManager.persist(booking2);
    }

    private static HotelRoom createRoom(String Triple, int capacity, int capacity1, Hotel hotel) {
        HotelRoom room2 = new HotelRoom();
        room2.setType(Triple);
        room2.setCapacity(capacity);
        room2.setBeds(capacity1);
        room2.setHotel(hotel);
        return room2;
    }

    @Test
    public void should_returnNothing_ifBookingOverlaps() {
        List<HotelRoom> foundRooms = find(9, 14);
        assertThat(foundRooms).isEmpty();
    }

    @Test
    public void should_returnRoom_ifBookingNotOverlaps() {
        assertThat(find(11, 14)).hasSize(1);
    }

    @Test
    public void should_returnNothing_ifBookingFullyOverlaps() {
        assertThat(find(3, 9)).isEmpty();
    }

    @Test
    public void should_returnNothing_ifBookingPartiallyOverlaps() {
        assertThat(find(7, 12)).isEmpty();
    }

    @Test
    public void should_returnNothing_ifBookingPartiallyOverlapsTwoBookings() {
        assertThat(find(8, 18)).isEmpty();
    }

    private List<HotelRoom> find(int checkIn, int checkOut) {
        return hotelRoomRepository.find(location, 20, getDayOfOctober(checkIn), getDayOfOctober(checkOut));
    }

    private static LocalDate getDayOfOctober(int dayOfMonth) {
        return LocalDate.of(2023, 10, dayOfMonth);
    }
}
