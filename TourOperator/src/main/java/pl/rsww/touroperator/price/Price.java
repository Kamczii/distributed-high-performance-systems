package pl.rsww.touroperator.price;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.rsww.touroperator.busses.lines.BusLine;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Price {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
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

}
