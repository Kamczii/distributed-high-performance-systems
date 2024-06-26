package pl.rsww.offerwrite.location;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "country", "city" }) })
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
