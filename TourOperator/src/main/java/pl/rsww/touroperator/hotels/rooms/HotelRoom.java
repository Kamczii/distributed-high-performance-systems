package pl.rsww.touroperator.hotels.rooms;

import pl.rsww.touroperator.hotels.Hotel;
import jakarta.persistence.*;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;

import java.util.Set;

@Entity
public class HotelRoom {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String description;
    private int maxPeople;
    private int numberOfBeds;
    private int numberInHotel;
    @ManyToOne
    @JoinColumn(name="hotel_id", nullable=false)
    private Hotel hotel;
    @OneToMany(mappedBy="hotelRoom", fetch = FetchType.EAGER)
    private Set<AgeRangePriceItem> priceList;

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

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public Set<AgeRangePriceItem> getPriceList() {
        return priceList;
    }

    public void setPriceList(Set<AgeRangePriceItem> priceList) {
        this.priceList = priceList;
    }
}
