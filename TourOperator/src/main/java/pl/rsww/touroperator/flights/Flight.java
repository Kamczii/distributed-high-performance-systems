package pl.rsww.touroperator.flights;

import lombok.Getter;
import lombok.Setter;
import pl.rsww.touroperator.flights.lines.FlightLine;
import jakarta.persistence.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @ManyToOne
    private FlightLine line;
    private LocalDate departureDate;
    private Boolean isItReturningFlight;
}
