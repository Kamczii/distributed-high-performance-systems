package pl.rsww.touroperator.hotels.rooms;

import lombok.Getter;
import lombok.Setter;
import pl.rsww.touroperator.hotels.Hotel;
import jakarta.persistence.*;
import pl.rsww.touroperator.price.AgeRangePriceItem;
import pl.rsww.touroperator.price.Price;

import java.util.Set;

@Getter
@Setter
@Entity
public class HotelRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String description;
    private int maxPeople;
    private int numberOfBeds;
    private int numberInHotel;
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    @OneToOne
    private Price price;

}
