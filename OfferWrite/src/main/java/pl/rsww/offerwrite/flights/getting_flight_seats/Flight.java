package pl.rsww.offerwrite.flights.getting_flight_seats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.rsww.offerwrite.core.events.EventMetadata;
import pl.rsww.offerwrite.core.views.VersionedView;
import pl.rsww.offerwrite.location.Location;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {@Index(columnList = "departure_id"), @Index(columnList = "destination_id")}, uniqueConstraints = @UniqueConstraint(columnNames = {"flightNumber","date"}))
public class Flight implements VersionedView {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String flightNumber;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "departure_id")
    private Location departure;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Location destination;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "flight")
    private List<FlightSeat> seats = new ArrayList<>();

    @JsonIgnore
    @Column(nullable = false)
    private long version;

    @JsonIgnore
    @Column(nullable = false)
    private long lastProcessedPosition;

    public void addSeat(FlightSeat seat) {
        seats.add(seat);
        seat.setFlight(this);
    }

    public void removeSeat(FlightSeat seat) {
        seats.remove(seat);
        seat.setFlight(null);
    }

    
    @JsonIgnore
    public void setMetadata(EventMetadata eventMetadata) {
        this.version =  eventMetadata.streamPosition();
        this.lastProcessedPosition = eventMetadata.logPosition();
    }
}
