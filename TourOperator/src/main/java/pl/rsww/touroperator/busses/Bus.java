package pl.rsww.touroperator.busses;

import jakarta.persistence.*;
import pl.rsww.touroperator.busses.lines.BusLine;

import java.time.LocalDate;

@Entity
public class Bus {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @ManyToOne
    private BusLine line;
    private LocalDate departureDate;
    private Boolean isItReturning;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BusLine getLine() {
        return line;
    }

    public void setLine(BusLine line) {
        this.line = line;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public Boolean getItReturning() {
        return isItReturning;
    }

    public void setItReturning(Boolean itReturning) {
        isItReturning = itReturning;
    }
}
