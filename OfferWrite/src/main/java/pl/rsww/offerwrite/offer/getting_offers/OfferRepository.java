package pl.rsww.offerwrite.offer.getting_offers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findAllByOrderById();

    @Query("SELECT o FROM Offer o LEFT JOIN FlightOffer fo ON o.id = fo.id " +
            "LEFT JOIN BusOffer bo ON o.id = bo.id " +
            "WHERE (fo.hotelRoom.id IN :rids OR bo.hotelRoom.id IN :rids)")
    List<Offer> findAllByHotelRoomIdIn(@Param("rids") Collection<UUID> hotelRoomIds);

    @Query("select o from Offer o left join FlightOffer fo ON o.id = fo.id left join BusOffer bo ON o.id = bo.id")
    List<Offer> findAll();
}
