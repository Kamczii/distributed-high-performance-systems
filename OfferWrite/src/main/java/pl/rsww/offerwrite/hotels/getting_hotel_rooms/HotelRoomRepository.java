package pl.rsww.offerwrite.hotels.getting_hotel_rooms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.rsww.offerwrite.location.Location;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HotelRoomRepository extends JpaRepository<HotelRoom, UUID> {
//    @Query("""
//            select r from HotelRoom r
//            where r.hotel.location = ?1 and r.capacity <= ?2 AND NOT EXISTS (
//                SELECT 1 FROM Booking b
//                WHERE b.room = r
//                  AND (?3 < b.checkOut AND b.checkIn < ?4)
//              )""")
//    List<HotelRoom> find(Location location, Integer maxCapacity, LocalDate checkIn, LocalDate checkOut); //todo

    List<HotelRoom> findAllByHotelLocationAndCapacityLessThanEqual(Location location, Integer maxCapacity);
    List<HotelRoom> findAllByTypeAndHotelId(String type, UUID uuid);
}
