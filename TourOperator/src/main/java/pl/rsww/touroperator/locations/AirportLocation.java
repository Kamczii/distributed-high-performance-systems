package pl.rsww.touroperator.locations;

import pl.rsww.touroperator.hotels.Hotel;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class AirportLocation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    int id;
    private String city;
    private String country;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
