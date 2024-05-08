package pl.rsww.touroperator.hotels.reservations;

import pl.rsww.touroperator.hotels.rooms.HotelRoom;
import org.springframework.data.repository.CrudRepository;

public interface RoomReservationRepository extends CrudRepository<RoomReservation, Integer> {
    Iterable<RoomReservation> findAllByRoom(HotelRoom room);
    long countByRoom(HotelRoom room);
//    long countByRoomAndReservationDate(HotelRoom room);
}
