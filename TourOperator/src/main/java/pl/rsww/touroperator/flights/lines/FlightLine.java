package pl.rsww.touroperator.flights.lines;

import pl.rsww.touroperator.hotels.Hotel;
import pl.rsww.touroperator.locations.AirportLocation;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class FlightLine {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
//    private String homeAirport;
//    private String destinationAirport;
    private int maxPassengers;
    @ManyToOne
    private AirportLocation destinationLocation;
    @ManyToOne
    private AirportLocation homeLocation;
    @ManyToMany(mappedBy = "hotelFlightLines")
    private Set<Hotel> hotels;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getHomeAirport() {
//        return homeAirport;
//    }
//
//    public void setHomeAirport(String homeAirport) {
//        this.homeAirport = homeAirport;
//    }

//    public String getDestinationAirport() {
//        return destinationAirport;
//    }
//
//    public void setDestinationAirport(String destinationAirport) {
//        this.destinationAirport = destinationAirport;
//    }

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

    public Set<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(Set<Hotel> hotels) {
        this.hotels = hotels;
    }

    public void addHotel(Hotel hotel){
        if(hotels != null){
            hotels.add(hotel);
        }
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
}
