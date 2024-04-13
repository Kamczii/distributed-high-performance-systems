package pl.rsww.offerwrite.api.requests;

import java.time.LocalDate;
import java.util.UUID;

public final class OfferRequests {
    public record OfferCreated(UUID hotelId,
                               String flightNumber,
                               LocalDate startDate,
                               LocalDate endDate) {

    }
}
