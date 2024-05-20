package pl.rsww.offerread.offers.getting_offers;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;
import pl.rsww.offerwrite.api.integration.AvailableOfferStatus;
import pl.rsww.offerwrite.api.integration.AvailableTransportType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferShortInfoDTO {

    private UUID id;

    private Hotel hotel;
    private Location departure;
    private Location destination;

    private LocalDate start;

    private LocalDate end;
    private AvailableOfferStatus status;
    private AvailableTransportType transport;

    private BigDecimal price;

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

    @Validated
    public record AgeRangePrice(
            @Nonnull Integer startingRange,
            @Nonnull Integer endingRange,
            @Nonnull BigDecimal price
    ){}
}
