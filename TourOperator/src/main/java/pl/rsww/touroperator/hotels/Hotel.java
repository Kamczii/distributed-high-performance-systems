package pl.rsww.touroperator.hotels;

import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;
import pl.rsww.touroperator.locations.AirportLocation;
import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    private String name;
    @OneToMany(mappedBy="hotel")
    private List<HotelRoom> rooms;
    @ManyToOne
    private AirportLocation location;
//    private String localRegionName;
//    @ManyToMany
//    @JoinTable(
//            name = "hotel_flight_lines",
//            joinColumns = @JoinColumn(name = "hotel_id"),
//            inverseJoinColumns = @JoinColumn(name = "flight_line_id"))
//    private Set<FlightLine> hotelFlightLines;



    public UUID getId() {return id;}

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public List<HotelRoom> getRooms() {
        if(rooms != null)
            return rooms;
        return new LinkedList<HotelRoom>();
    }

    public void setRooms(List<HotelRoom> rooms) {
        this.rooms = rooms;
    }

    public AirportLocation getLocation() {
        return location;
    }

    public void setLocation(AirportLocation location) {
        this.location = location;
    }

//    public Set<FlightLine> getFlightLines() {
//        return hotelFlightLines;
//    }
//
//    public void setFlightLines(Set<FlightLine> flightLines) {
//        this.hotelFlightLines = flightLines;
//    }
//
//    public Set<FlightLine> getHotelFlightLines() {
//        return hotelFlightLines;
//    }
//
//    public void setHotelFlightLines(Set<FlightLine> hotelFlightLines) {
//        this.hotelFlightLines = hotelFlightLines;
//    }

//    public String getLocalRegionName() {
//        return localRegionName;
//    }
//
//    public void setLocalRegionName(String localRegionName) {
//        this.localRegionName = localRegionName;
//    }

}
