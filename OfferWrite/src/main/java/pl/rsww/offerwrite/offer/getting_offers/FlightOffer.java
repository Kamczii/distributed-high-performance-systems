package pl.rsww.offerwrite.offer.getting_offers;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"initial_flight_id","return_flight_id","hotel_room_id"}))
public class FlightOffer extends Offer {

    @ManyToOne
    @JoinColumn(name = "initial_flight_id")
    private Flight initialFlight;

    @ManyToOne
    @JoinColumn(name = "return_flight_id")
    private Flight returnFlight;

    @ManyToOne
    @JoinColumn(name = "hotel_room_id")
    private HotelRoom hotelRoom;
}