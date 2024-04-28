package pl.rsww.offerread.offers.getting_offers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.rsww.offerwrite.api.AvailableOrderStatus;

import java.time.LocalDate;
import java.util.UUID;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferShortInfo {
    @Id
    private UUID id;

    private Hotel hotel;
    private Location departure;
    private Location destination;

    private LocalDate start;

    private LocalDate end;
    private AvailableOrderStatus status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Hotel {
        private String name;
        private Room room;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Room {
        private String type;
        private Integer capacity;
        private Integer beds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private String city;
        private String country;
    }
}
