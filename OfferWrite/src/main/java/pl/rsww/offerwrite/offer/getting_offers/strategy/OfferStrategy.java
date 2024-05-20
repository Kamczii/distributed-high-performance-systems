package pl.rsww.offerwrite.offer.getting_offers.strategy;

import pl.rsww.offerwrite.api.integration.AvailableTransportType;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.offer.getting_offers.Offer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface OfferStrategy<T> {
    Location getInitialLocation(Offer offer);
    Location getDestinationLocation(Offer offer);
    LocalDate getStartDate(Offer offer);
    LocalDate getEndDate(Offer offer);
    int getAvailableSeats(Offer offer);
    T cast(Offer offer);

    void lock(Offer offer, UUID orderId, int visitorsCount);

    void confirmLock(Offer offer, UUID orderId);

    boolean isOfferAvailable(Offer offer);

    BigDecimal calculatePrice(Offer offer, Collection<Integer> ageOfVisitors);

    Collection<AgeRangePrice> getPriceList(Offer offer);

    AvailableTransportType getTransport(Offer offer);
}
