package pl.rsww.touroperator.busses.lines;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.rsww.touroperator.price.AgeRangePriceItem;
import pl.rsww.touroperator.locations.AirportLocation;
import pl.rsww.touroperator.price.Price;

import java.util.Set;

@Getter
@Setter
@Entity
public class BusLine {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private int maxPassengers;
    @ManyToOne
    private AirportLocation destinationLocation;
    @ManyToOne
    private AirportLocation homeLocation;
    @OneToOne
    private Price price;

    public String busNumber(){
        return "RB" + id;
    }

}
