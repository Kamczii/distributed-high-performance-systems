package pl.rsww.offerwrite.hotels.getting_hotel_rooms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {

}
