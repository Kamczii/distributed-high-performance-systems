package pl.rsww.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.rsww.preference.model.Destination;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, UUID> {
    @Query("SELECT d FROM Destination d ORDER BY d.occurrences DESC")
    List<Destination> findPreferenceDestination();

    Optional<Destination> findByCountryAndCity(String country, String city);
}
