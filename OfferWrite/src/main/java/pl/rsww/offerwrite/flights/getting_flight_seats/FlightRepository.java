package pl.rsww.offerwrite.flights.getting_flight_seats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.rsww.offerwrite.location.Location;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FlightRepository extends JpaRepository<Flight, UUID> {
    @Query("select f from Flight f where f.destination = ?1 and f.departure = ?2 and f.date between ?3 and ?4")
    List<Flight> findByDestinationAndDepartureAndDateBetween(Location destination, Location departure, LocalDate dateStart, LocalDate dateEnd);

}
