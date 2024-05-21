package pl.rsww.offerread.offers.getting_offers;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;
import pl.rsww.offerwrite.api.integration.AvailableOfferStatus;
import pl.rsww.offerwrite.api.integration.AvailableTransportType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferShortInfo {
    @Id
    private UUID id;

    @Indexed(unique = true)
    private UUID offerId;

    private Hotel hotel;
    private Location departure;
    private Location destination;

    private LocalDate start;

    private LocalDate end;
    private AvailableOfferStatus status;
    private AvailableTransportType transport;

    private Collection<AgeRangePrice> priceList;

    public OfferShortInfo changeStatus(AvailableOfferStatus status) {
        setStatus(status);
        return this;
    }

    public BigDecimal getPrice(int age) {
        return priceList.stream().filter(price -> price.startingRange() <= age && age <= price.endingRange()).findAny()
                .map(AgeRangePrice::price)
                .orElse(BigDecimal.ZERO);
    }

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
