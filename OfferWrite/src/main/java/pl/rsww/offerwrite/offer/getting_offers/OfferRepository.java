package pl.rsww.offerwrite.offer.getting_offers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {
    @Query("select o from Offer o where o.hotelRoom.capacity > ?1 and (o.initialFlight.id = ?2 or o.returnFlight.id = ?3)")
    List<Offer> findAllByHotelRoomCapacityGreaterThanAndInitialFlightIdOrReturnFlightId(Integer capacity, UUID initialFlightId, UUID returnFlightId);

    List<Offer> findAllByInitialFlightIdOrReturnFlightId(UUID id, UUID id1);

    List<Offer> findAllByHotelRoomCapacityLessThanAndInitialFlightIdOrReturnFlightId(Integer capacity, UUID id, UUID id1);

    List<Offer> findAllByOrderById();

    List<Offer> findAllByHotelRoomIdIn(Collection<UUID> hotelRoomIds);
}
