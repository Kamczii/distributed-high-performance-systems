package pl.rsww.touroperator.locations;

import lombok.Getter;
import lombok.Setter;
import pl.rsww.touroperator.hotels.Hotel;
import jakarta.persistence.*;

import java.util.List;

@Getter
@Setter
@Entity
public class AirportLocation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    int id;
    private String city;
    private String country;
}
