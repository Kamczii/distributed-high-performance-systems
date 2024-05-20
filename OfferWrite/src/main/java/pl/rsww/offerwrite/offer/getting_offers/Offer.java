package pl.rsww.offerwrite.offer.getting_offers;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.data.domain.DomainEvents;
import pl.rsww.offerwrite.domain.AbstractDomainEvent;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.hotels.getting_hotel_rooms.HotelRoom;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Offer {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @DomainEvents
    public List<AbstractDomainEvent> domainEvents() {
        return Collections.singletonList(new OfferEvent(id));
    }

    public HotelRoom getHotelRoom() {
        if (this instanceof FlightOffer flightOffer) {
            return flightOffer.getHotelRoom();
        } else if (this instanceof BusOffer busOffer) {
            return busOffer.getHotelRoom();
        };
        throw new NotImplementedException();
    }
}