package pl.rsww.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.rsww.preference.model.Destination;
import pl.rsww.preference.model.Room;
import pl.rsww.preference.model.Hotel;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID> {
    @Query("SELECT h FROM Hotel h ORDER BY h.occurrences DESC")
    Optional<Hotel> findPreferenceHotel();
    Optional<Hotel> findByNameAndRoom(String name, Room room);
}
