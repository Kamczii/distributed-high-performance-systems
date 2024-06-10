package pl.rsww.offerwrite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRoom;
import pl.rsww.tour_operator.api.HotelRequests;
import pl.rsww.offerwrite.hotels.Hotel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HotelTest {
    public static final String SINGLE_ROOM_TYPE = "Single";
    private static final String DOUBLE_ROOM_TYPE = "Double";
    private UUID hotelId;
    private String name;
    private HotelRequests.LocationRequest location;

    private Hotel hotel;
    private HotelRequests.CreateHotel createHotelRequest;

    @BeforeEach
    void setUp() {
        hotelId = UUID.randomUUID();
        name = "Test Hotel";
        location = new HotelRequests.LocationRequest("Test Country", "Test City");
        createHotelRequest = new HotelRequests.CreateHotel(hotelId, name, location, List.of(
                new HotelRequests.RoomRequest(SINGLE_ROOM_TYPE, 1, 1, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))),
                new HotelRequests.RoomRequest(SINGLE_ROOM_TYPE, 1, 1, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))),
                new HotelRequests.RoomRequest(DOUBLE_ROOM_TYPE, 2, 2, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN)))
        ), List.of(HotelRequests.ModesOfTransport.AIRPLANE));
        
        hotel = Hotel.create(createHotelRequest);
    }

    @Test
    void testCreateHotel() {

        // Assert
        assertEquals(hotelId, hotel.id());
        assertEquals(name, hotel.getName());
        assertEquals(location.city(), hotel.getLocation().city());
        assertEquals(createHotelRequest.rooms().size(), hotel.getRooms().rooms().stream().map(HotelRoom::count).reduce(0L, Long::sum));
        assertTrue(hotel.getActiveReservations().values()
                .stream()
                .allMatch(List::isEmpty));
    }

    @Test
    void testLockRoom() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = LocalDate.now().plusDays(1);

        // Act
        hotel.lock(orderId, SINGLE_ROOM_TYPE, checkInDate, checkOutDate);

        // Assert
        assertEquals(1, getReservationCount(SINGLE_ROOM_TYPE)
        );
    }

    @Test
    void testConfirmLock() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = LocalDate.now().plusDays(1);
        hotel.lock(orderId, SINGLE_ROOM_TYPE, checkInDate, checkOutDate);

        // Act
        hotel.confirmLock(orderId);

        // Assert
        assertEquals(1, getReservationCount(SINGLE_ROOM_TYPE));
    }

    @Test
    void testConfirmLock_ReservationNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();

        // Assert
        assertThrows(IllegalStateException.class, () -> hotel.confirmLock(orderId));
    }

    @Test
    void testLockOverlappingRooms() {
        // Arrange
        UUID orderId1 = UUID.randomUUID();
        UUID orderId2 = UUID.randomUUID();
        UUID orderId3 = UUID.randomUUID();
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = LocalDate.now().plusDays(1);
        hotel.lock(orderId1, SINGLE_ROOM_TYPE, checkInDate, checkOutDate);
        hotel.lock(orderId2, SINGLE_ROOM_TYPE, checkInDate, checkOutDate);

        // Assert
        assertThrows(IllegalStateException.class, () -> hotel.lock(orderId3, SINGLE_ROOM_TYPE, checkInDate, checkOutDate));
    }

    @Test
    void lock_two_same_rooms_should_result_in_two_reservations() {
        // Arrange
        UUID orderId1 = UUID.randomUUID();
        UUID orderId2 = UUID.randomUUID();
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = LocalDate.now().plusDays(1);
        hotel.lock(orderId1, SINGLE_ROOM_TYPE, checkInDate, checkOutDate);
        hotel.lock(orderId2, SINGLE_ROOM_TYPE, checkInDate, checkOutDate);

        // Assert
        assertEquals(2, getReservationCount(SINGLE_ROOM_TYPE));
    }

    @Test
    void lock_all_rooms_should_result_in_four_reservations() {
        // Arrange
        final var order1 = UUID.randomUUID();
        final var order2 = UUID.randomUUID();
        final var order3 = UUID.randomUUID();
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = LocalDate.now().plusDays(1);
        hotel.lock(order1, SINGLE_ROOM_TYPE, checkInDate, checkOutDate);
        hotel.lock(order2, SINGLE_ROOM_TYPE, checkInDate, checkOutDate);
        hotel.lock(order3, DOUBLE_ROOM_TYPE, checkInDate, checkOutDate);

        hotel.confirmLock(order3);
        hotel.confirmLock(order2);
        hotel.confirmLock(order1);

        // Assert
        assertEquals(2, getReservationCount(SINGLE_ROOM_TYPE));
        assertEquals(1, getReservationCount(DOUBLE_ROOM_TYPE));
    }

    @Test
    void lock_non_overlying_reservations() {
        // Arrange
        LocalDate dateOne = LocalDate.now();
        LocalDate dateTwo = LocalDate.now().plusDays(1);
        LocalDate dateThree = LocalDate.now().plusDays(2);
        hotel.lock(UUID.randomUUID(), DOUBLE_ROOM_TYPE, dateOne, dateTwo);
        hotel.lock(UUID.randomUUID(), DOUBLE_ROOM_TYPE, dateTwo, dateThree);

        // Assert
        assertEquals(2, getReservationCount(DOUBLE_ROOM_TYPE));
    }

    private long getReservationCount(String roomType) {
        return hotel.getActiveReservations().get(roomType).size();
    }

}

