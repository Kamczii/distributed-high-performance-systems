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
//    @OneToMany
//    private List<Hotel> hotels;

    public java.lang.String getCity() {
        return city;
    }

    public void setCity(java.lang.String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

//    public List<Hotel> getHotels() {
//        return hotels;
//    }

//    public void setHotels(List<Hotel> hotels) {
//        this.hotels = hotels;
//    }

    public void setCountry(String country) {
        this.country = country;
    }

//    public void addHotel(Hotel hotel){
//        if(hotels != null){
//            hotels.add(hotel);
//        }
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
