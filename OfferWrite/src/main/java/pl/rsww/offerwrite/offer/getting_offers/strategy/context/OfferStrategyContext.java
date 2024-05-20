package pl.rsww.offerwrite.offer.getting_offers.strategy.context;


import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.strategy.OfferStrategy;

public interface OfferStrategyContext {
    OfferStrategy<? extends Offer> resolve(Offer clazz);
}
