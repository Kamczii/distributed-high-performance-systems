package pl.rsww.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.rsww.preference.model.Room;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    Optional<Room> findByTypeAndCapacityAndBeds(String type, Integer capacity, Integer beds);

}
