package pl.rsww.offerwrite.flights.getting_flight_seats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.rsww.offerwrite.core.events.EventMetadata;
import pl.rsww.offerwrite.core.projections.IdentifiableEntity;
import pl.rsww.offerwrite.core.views.VersionedView;
import pl.rsww.offerwrite.flights.FlightUtils;
import pl.rsww.offerwrite.location.Location;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {@Index(columnList = "departure_id"), @Index(columnList = "destination_id")}, uniqueConstraints = @UniqueConstraint(columnNames = {"flightNumber","date"}))
public class Flight implements VersionedView, IdentifiableEntity {
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

    @JsonIgnore
    @Column(nullable = false)
    private long version;

    @JsonIgnore
    @Column(nullable = false)
    private long lastProcessedPosition;

    
    @JsonIgnore
    public void setMetadata(EventMetadata eventMetadata) {
        this.version =  eventMetadata.streamPosition();
        this.lastProcessedPosition = eventMetadata.logPosition();
    }

    public String getFlightId() {
        return FlightUtils.flightId(flightNumber, date);
    }
}
