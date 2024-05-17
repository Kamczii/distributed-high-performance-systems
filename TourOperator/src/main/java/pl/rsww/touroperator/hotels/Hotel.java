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
    @OneToMany(mappedBy="hotel", fetch = FetchType.EAGER)
    private List<HotelRoom> rooms;
    @ManyToOne
    private AirportLocation location;
    private ModesOfTransport modeOfTransport;

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

    public ModesOfTransport getModeOfTransport() {
        return modeOfTransport;
    }

    public void setModeOfTransport(ModesOfTransport modeOfTransport) {
        this.modeOfTransport = modeOfTransport;
    }
}
