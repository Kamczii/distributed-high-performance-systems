package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;
import pl.rsww.offerwrite.offer.getting_offers.strategy.context.impl.OfferStrategyContextImpl;

import java.util.Collection;
import java.util.UUID;

@Slf4j

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferStrategyContextImpl offerStrategyContext;

    public void reserveOffer(UUID offerId, UUID orderId, Collection<Integer> ageOfVisitors) {
        final var offer = fetchOffer(offerId);
        final var capacity = offer.getHotelRoom().getCapacity();

        final var visitorsCount = ageOfVisitors.size();
        if (visitorsCount > capacity) {
            throw new IllegalStateException(String.format("The number of people exceeds the capacity, required %d, got %d. (Offer %s, Order %s)", visitorsCount, capacity, offerId, orderId));
        }

        offerStrategyContext.resolve(offer)
                .lock(offer, orderId, visitorsCount);
    }

    public void confirmOffer(UUID offerId, UUID orderId) {
        final var offer = fetchOffer(offerId);
        offerStrategyContext.resolve(offer)
                .confirmLock(offer, orderId);
    }


    private Offer fetchOffer(UUID offerId) {
        return offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException(Offer.class.toString(), offerId.toString()));
    }

}
