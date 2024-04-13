package pl.rsww.offerwrite.flights.getting_flight_seats;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSeat {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private SeatState seatState;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Flight flight;
}
