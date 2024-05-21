package pl.rsww.offerwrite.buses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.rsww.offerwrite.buses.getting_bus_seats.Bus;
import pl.rsww.offerwrite.location.Location;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BusRepository extends JpaRepository<Bus, UUID> {
    @Query("select b from Bus b where b.destination = ?1 and b.departure = ?2 and b.date between ?3 and ?4")
    List<Bus> findByDestinationAndDepartureAndDateBetween(Location destination, Location departure, LocalDate dateStart, LocalDate dateEnd);
}
