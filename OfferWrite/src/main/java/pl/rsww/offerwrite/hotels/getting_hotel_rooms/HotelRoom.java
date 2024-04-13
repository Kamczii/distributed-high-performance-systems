package pl.rsww.offerwrite.hotels.getting_hotel_rooms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.rsww.offerwrite.core.events.EventMetadata;
import pl.rsww.offerwrite.core.views.VersionedView;
import pl.rsww.offerwrite.flights.getting_flight_seats.FlightSeat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelRoom implements VersionedView {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String type;

    private Integer capacity;
    private Integer beds;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Hotel hotel;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "room")
    private List<Booking> bookings = new ArrayList<>();

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
}
