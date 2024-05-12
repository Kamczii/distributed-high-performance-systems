package pl.rsww.offerwrite.api.integration;

import jakarta.annotation.Nonnull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public sealed interface OfferIntegrationEvent {
    record Created(UUID offerId,
                   Hotel hotel,
                   Location departure,
                   Location destination,
                   LocalDate start,
                   LocalDate end,
                   AvailableOfferStatus status,
                   Collection<AgeRangePrice> priceList

    ) implements OfferIntegrationEvent {}

    record StatusChanged(UUID offerId,
                         AvailableOfferStatus status
    ) implements OfferIntegrationEvent {}





    record Hotel (String name, Room room) {

    }

    record Room(String type, Integer capacity, Integer beds) {

    }

    record Location(String city, String country) {

    }
    @Validated
    record AgeRangePrice(
            @Nonnull Integer startingRange,
            @Nonnull Integer endingRange,
            @Nonnull BigDecimal price
    ){}
}