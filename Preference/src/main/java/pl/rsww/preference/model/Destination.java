package pl.rsww.preference.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "destinations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String country;
    private String city;
    @Builder.Default
    private Integer occurrences = 0;
}
