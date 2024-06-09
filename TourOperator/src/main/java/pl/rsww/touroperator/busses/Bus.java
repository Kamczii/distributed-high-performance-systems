package pl.rsww.touroperator.busses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.rsww.touroperator.busses.lines.BusLine;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Bus {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @ManyToOne
    private BusLine line;
    private LocalDate departureDate;
    private Boolean isItReturning;
}
