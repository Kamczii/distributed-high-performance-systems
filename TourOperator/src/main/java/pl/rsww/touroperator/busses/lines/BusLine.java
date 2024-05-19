package pl.rsww.touroperator.busses.lines;

import jakarta.persistence.*;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.locations.AirportLocation;

import java.util.Set;

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
    @OneToMany(mappedBy="busLine", fetch = FetchType.EAGER)
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

    public String busNumber(){
        return "RB" + id;
    }

    public Set<AgeRangePriceItem> getPriceList() {
        return priceList;
    }

    public void setPriceList(Set<AgeRangePriceItem> priceList) {
        this.priceList = priceList;
    }
}
