package pl.rsww.touroperator.hotels;

import lombok.Getter;
import lombok.Setter;
import pl.rsww.touroperator.data.ModesOfTransportSetting;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;
import pl.rsww.touroperator.locations.AirportLocation;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
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
    private ModesOfTransportSetting modeOfTransport;

}
