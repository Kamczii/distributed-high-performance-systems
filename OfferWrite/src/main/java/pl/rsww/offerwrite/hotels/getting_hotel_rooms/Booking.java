package pl.rsww.offerwrite.hotels.getting_hotel_rooms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.rsww.offerwrite.core.events.EventMetadata;
import pl.rsww.offerwrite.core.views.VersionedView;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Booking implements VersionedView {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private LocalDate checkIn;
    private LocalDate checkOut;
    private LocalDate lockedAt;

    @ManyToOne
    @JoinColumn(nullable = false)
    private HotelRoom room;

    private boolean lock;
    private boolean confirmed;

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
