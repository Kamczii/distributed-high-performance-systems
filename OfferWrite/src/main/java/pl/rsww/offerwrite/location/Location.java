package pl.rsww.offerwrite.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pl.rsww.offerwrite.core.events.EventMetadata;
import pl.rsww.offerwrite.core.views.VersionedView;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "country", "city" }) })
public class Location {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String country;
    private String city;

    @Override
    public String toString() {
        return "%s/%s".formatted(city, country);
    }
}
