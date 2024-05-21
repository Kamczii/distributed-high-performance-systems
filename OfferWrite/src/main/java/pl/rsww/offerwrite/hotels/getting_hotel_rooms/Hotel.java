package pl.rsww.offerwrite.hotels.getting_hotel_rooms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.rsww.offerwrite.core.events.EventMetadata;
import pl.rsww.offerwrite.core.projections.IdentifiableEntity;
import pl.rsww.offerwrite.core.views.VersionedView;
import pl.rsww.offerwrite.location.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(columnList = "location_id"))
public class Hotel implements VersionedView, IdentifiableEntity {
    @Id
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "hotel")
    private List<HotelRoom> rooms = new ArrayList<>();

    @JsonIgnore
    @Column(nullable = false)
    private long version;

    @JsonIgnore
    @Column(nullable = false)
    private long lastProcessedPosition;

    public void addHotelRoom(HotelRoom hotelRoom) {
        rooms.add(hotelRoom);
        hotelRoom.setHotel(this);
    }

    public void removeHotelRoom(HotelRoom hotelRoom) {
        rooms.remove(hotelRoom);
        hotelRoom.setHotel(null);
    }

    
    @JsonIgnore
    public void setMetadata(EventMetadata eventMetadata) {
        this.version =  eventMetadata.streamPosition();
        this.lastProcessedPosition = eventMetadata.logPosition();
    }
}
