package pl.rsww.offerwrite.flights.getting_flight_seats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.rsww.offerwrite.location.Location;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FlightSeatRepository extends JpaRepository<FlightSeat, UUID> {
    int countByFlightIdAndSeatStateState(UUID flight, AvailableSeatState state);
}
