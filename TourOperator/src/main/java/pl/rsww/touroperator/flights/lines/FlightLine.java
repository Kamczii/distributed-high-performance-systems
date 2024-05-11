package pl.rsww.touroperator.flights.lines;

import pl.rsww.touroperator.hotels.Hotel;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.locations.AirportLocation;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class FlightLine {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private int maxPassengers;
    @ManyToOne
    private AirportLocation destinationLocation;
    @ManyToOne
    private AirportLocation homeLocation;
    @OneToMany(mappedBy="flightLine")
    private Set<AgeRangePriceItem> priceList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public AirportLocation getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(AirportLocation destinationLocation) {
        this.destinationLocation = destinationLocation;
    }


    public AirportLocation getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(AirportLocation homeLocation) {
        this.homeLocation = homeLocation;
    }

    public String flightNumber(){
        return "RA" + id;
    }

    public Set<AgeRangePriceItem> getPriceList() {
        return priceList;
    }

    public void setPriceList(Set<AgeRangePriceItem> priceList) {
        this.priceList = priceList;
    }
}
