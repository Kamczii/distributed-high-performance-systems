package pl.rsww.offerwrite.offer.getting_offers;

import pl.rsww.offerwrite.domain.AbstractDomainEvent;

import java.util.UUID;

public class OfferEvent extends AbstractDomainEvent {
    public OfferEvent(UUID entityId) {
        super(entityId);
    }
}
