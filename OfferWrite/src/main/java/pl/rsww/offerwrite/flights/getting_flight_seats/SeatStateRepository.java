package pl.rsww.offerwrite.flights.getting_flight_seats;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeatStateRepository extends JpaRepository<SeatState, UUID> {
    SeatState findByState(AvailableSeatState state);
}
