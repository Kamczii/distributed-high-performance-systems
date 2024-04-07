package pl.rsww.offerwrite.offer;

import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;

import java.util.UUID;

public class Offer extends AbstractAggregate<OfferEvent, UUID> {

    @Override
    public void when(OfferEvent offerEvent) {

    }

}
