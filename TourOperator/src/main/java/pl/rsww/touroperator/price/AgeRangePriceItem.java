package pl.rsww.touroperator.price;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.rsww.touroperator.busses.lines.BusLine;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AgeRangePriceItem {
    private Integer startingRange;
    private Integer endingRange;
    private BigDecimal referencePrice;
    private BigDecimal fraction;

    public BigDecimal getPrice(){
        return this.referencePrice.multiply(this.fraction).round(PriceTools.getContext());
    }
}
