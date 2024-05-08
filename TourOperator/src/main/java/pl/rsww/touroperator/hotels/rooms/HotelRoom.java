package pl.rsww.touroperator.hotels.rooms;

import pl.rsww.touroperator.hotels.Hotel;
import jakarta.persistence.*;

@Entity
public class HotelRoom {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String description;
    private int maxPeople;
    private int numberInHotel;
    @ManyToOne
    @JoinColumn(name="hotel_id", nullable=false)
    private Hotel hotel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public int getNumberInHotel() {
        return numberInHotel;
    }

    public void setNumberInHotel(int numberInHotel) {
        this.numberInHotel = numberInHotel;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
