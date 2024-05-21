package pl.rsww.offerwrite.offer.getting_offers.strategy.context.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.offer.getting_offers.BusOffer;
import pl.rsww.offerwrite.offer.getting_offers.FlightOffer;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.strategy.OfferStrategy;
import pl.rsww.offerwrite.offer.getting_offers.strategy.context.OfferStrategyContext;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OfferStrategyContextImpl implements OfferStrategyContext {
	private final OfferStrategy<FlightOffer> flightOfferOfferStrategyImpl;
	private final OfferStrategy<BusOffer> busOfferOfferStrategyImpl;

    private final Map<Class<? extends Offer>, OfferStrategy<? extends Offer>> context = new HashMap<>();

    @Override
    public OfferStrategy<? extends Offer> resolve(Offer clazz) {
        final var exists = context.containsKey(clazz.getClass());
        if (exists) {
            return context.get(clazz.getClass());
        } else {
            throw new ResourceNotFoundException(clazz.getClass().getSimpleName());
        }
    }

    @PostConstruct
    private void initializeContext() {
        this.context.put(FlightOffer.class, flightOfferOfferStrategyImpl);
        this.context.put(BusOffer.class, busOfferOfferStrategyImpl);
    }
}
