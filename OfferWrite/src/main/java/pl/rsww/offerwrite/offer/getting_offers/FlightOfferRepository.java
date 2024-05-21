package pl.rsww.offerwrite.offer.getting_offers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface FlightOfferRepository extends JpaRepository<FlightOffer, UUID> {
    @Query("select o from FlightOffer o where o.hotelRoom.capacity > ?1 and (o.initialFlight.id = ?2 or o.returnFlight.id = ?3)")
    List<Offer> findAllByHotelRoomCapacityGreaterThanAndInitialFlightIdOrReturnFlightId(Integer capacity, UUID initialFlightId, UUID returnFlightId);

}
