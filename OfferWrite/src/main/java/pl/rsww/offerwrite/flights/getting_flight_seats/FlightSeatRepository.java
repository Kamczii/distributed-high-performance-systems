package pl.rsww.offerwrite.flights.getting_flight_seats;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FlightSeatRepository extends JpaRepository<FlightSeat, UUID> {
    int countByFlightIdAndSeatStateState(UUID flight, AvailableSeatState state);
}
