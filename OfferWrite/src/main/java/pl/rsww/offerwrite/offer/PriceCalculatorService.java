package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePriceHelper;
import pl.rsww.offerwrite.flights.Flight;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.hotels.Hotel;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;
import pl.rsww.offerwrite.offer.getting_offers.strategy.context.impl.OfferStrategyContextImpl;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PriceCalculatorService {
    private final OfferRepository offerRepository;
    private final OfferStrategyContextImpl offerStrategyContext;

    public BigDecimal calculate(UUID offerId, Collection<Integer> ageOfVisitors) {
        final var offer = fetchOffer(offerId);
        return offerStrategyContext.resolve(offer)
                .calculatePrice(offer, ageOfVisitors);
    }

    private Offer fetchOffer(UUID offerId) {
        return offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException(Offer.class.toString(), offerId.toString()));
    }


}
