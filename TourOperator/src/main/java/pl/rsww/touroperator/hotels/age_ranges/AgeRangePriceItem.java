package pl.rsww.touroperator.hotels.age_ranges;

import jakarta.persistence.*;
import pl.rsww.touroperator.hotels.Hotel;

import java.math.BigDecimal;

@Entity
public class AgeRangePriceItem {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private Integer startingRange;
    private Integer endingRange;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name="hotel_id", nullable=false)
    private Hotel hotel;

    public Integer getStartingRange() {
        return startingRange;
    }

    public void setStartingRange(Integer startingRange) {
        this.startingRange = startingRange;
    }

    public Integer getEndingRange() {
        return endingRange;
    }

    public void setEndingRange(Integer endingRange) {
        this.endingRange = endingRange;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public AgeRangePriceItem() {
    }

    public AgeRangePriceItem(Integer startingRange, Integer endingRange, BigDecimal price) {
        this.startingRange = startingRange;
        this.endingRange = endingRange;
        this.price = price;
    }
}
