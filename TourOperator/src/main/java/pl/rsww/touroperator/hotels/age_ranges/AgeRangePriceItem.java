package pl.rsww.touroperator.hotels.age_ranges;

import jakarta.persistence.*;
import pl.rsww.touroperator.busses.lines.BusLine;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.hotels.Hotel;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;

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
    @JoinColumn(name="hotel_room_id", nullable=true)
    private HotelRoom hotelRoom;
    @ManyToOne
    @JoinColumn(name="flight_line_id", nullable=true)
    private FlightLine flightLine;
    @ManyToOne
    @JoinColumn(name="bus_line_id", nullable=true)
    private BusLine busLine;

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


    public AgeRangePriceItem() {
    }

    public AgeRangePriceItem(Integer startingRange, Integer endingRange, BigDecimal price) {
        this.startingRange = startingRange;
        this.endingRange = endingRange;
        this.price = price;
    }

    public HotelRoom getRoom() {
        return hotelRoom;
    }

    public void setRoom(HotelRoom room) {
        this.hotelRoom = room;
    }

    public FlightLine getFlightLine() {
        return flightLine;
    }

    public void setFlightLine(FlightLine flightLine) {
        this.flightLine = flightLine;
    }

    public HotelRoom getHotelRoom() {
        return hotelRoom;
    }

    public void setHotelRoom(HotelRoom hotelRoom) {
        this.hotelRoom = hotelRoom;
    }

    public BusLine getBusLine() {
        return busLine;
    }

    public void setBusLine(BusLine busLine) {
        this.busLine = busLine;
    }
}
