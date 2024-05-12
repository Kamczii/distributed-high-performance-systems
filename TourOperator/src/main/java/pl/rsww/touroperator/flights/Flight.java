package pl.rsww.touroperator.flights;

import pl.rsww.touroperator.flights.lines.FlightLine;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @ManyToOne
    private FlightLine line;
    private LocalDate departureDate;
    private Boolean isItReturningFlight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FlightLine getLine() {
        return line;
    }

    public void setLine(FlightLine line) {
        this.line = line;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public Boolean getItReturningFlight() {
        return isItReturningFlight;
    }

    public void setItReturningFlight(Boolean itReturningFlight) {
        isItReturningFlight = itReturningFlight;
    }
}
