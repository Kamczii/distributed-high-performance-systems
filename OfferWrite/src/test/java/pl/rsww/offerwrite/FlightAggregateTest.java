package pl.rsww.offerwrite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import pl.rsww.tour_operator.api.FlightRequests;
import pl.rsww.offerwrite.flights.Flight;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlightAggregateTest {

    private Flight flight;
    private final LocalDate flightDate = LocalDate.now();
    private final FlightRequests.LocationRequest departure = new FlightRequests.LocationRequest("Country1", "City1");
    private final FlightRequests.LocationRequest destination = new FlightRequests.LocationRequest("Country2", "City2");
    private final String flightNumber = "FL123";
    private final Integer capacity = 100;

    @BeforeEach
    void setUp() {
        flight = Flight.create(new FlightRequests.CreateFlight(
                flightNumber,
                capacity,
                departure,
                destination,
                flightDate,
                List.of(new FlightRequests.AgeRangePrice(0, 100, BigDecimal.TEN))
        ));
    }

    @Test
    void testCreateFlight() {
        assertEquals(departure.city(), flight.getDeparture().city());
        assertEquals(destination.city(), flight.getDestination().city());
        assertEquals(flightNumber, flight.getFlightNumber());
        assertEquals(flightDate, flight.getDate());
        assertEquals(capacity, flight.getCapacity());
    }

    @Test
    void testReserveSeats() {
        UUID orderId = UUID.randomUUID();
        int seatsToReserve = 5;
        flight.lock(seatsToReserve, orderId);
        assertEquals(capacity - seatsToReserve, flight.getCapacity());
        assertTrue(flight.getActiveReservations().contains(Pair.of(orderId, seatsToReserve)));
    }

    @Test
    void testConfirmReservation() {
        UUID orderId = UUID.randomUUID();
        int seatsToConfirm = 5;
        flight.lock(seatsToConfirm, orderId);
        flight.confirmLock(seatsToConfirm, orderId);
        assertEquals(capacity - seatsToConfirm, flight.getCapacity());
        assertTrue(flight.getActiveConfirmations().contains(Pair.of(orderId, seatsToConfirm)));
    }

    @Test
    void testReserveSeats_noSeatsAvailable() {
        int seatsToReserve = capacity + 1;
        UUID orderId = UUID.randomUUID();
        Exception exception = assertThrows(IllegalStateException.class, () -> flight.lock(seatsToReserve, orderId));
        String expectedMessage = "No available seats";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testReleaseSeats() {
        UUID orderId = UUID.randomUUID();
        int seatsToReserve = 5;
        flight.lock(seatsToReserve, orderId);
        flight.releaseLock(seatsToReserve, orderId);
        assertEquals(capacity, flight.getCapacity());
        assertFalse(flight.getActiveReservations().contains(Pair.of(orderId, seatsToReserve)));
    }

    @Test
    void testCancelConfirmation() {
        UUID orderId = UUID.randomUUID();
        int seatsToConfirm = 5;
        flight.lock(seatsToConfirm, orderId);
        flight.confirmLock(seatsToConfirm, orderId);
        flight.cancelConfirmation(seatsToConfirm, orderId);
        assertEquals(capacity, flight.getCapacity());
        assertFalse(flight.getActiveConfirmations().contains(Pair.of(orderId, seatsToConfirm)));
    }

    @Test
    void testReservationNotFound() {
        UUID orderId = UUID.randomUUID();
        int seatsToConfirm = 5;
        assertThrows(IllegalStateException.class, () -> flight.confirmLock(seatsToConfirm, orderId));
    }
}

