package pl.rsww.offerread.locations.getting_locations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(def = "{'city' : 1, 'country' : 1}", unique = true)
public class Location {
    String city;
    String country;
}
