package pl.rsww.offerread.offers.getting_offers;

import pl.rsww.offerread.events.EventMetadata;
import pl.rsww.offerread.views.VersionedView;

public class OfferShortInfo implements VersionedView {

    @Override
    public long getLastProcessedPosition() {
        return 0;
    }

    @Override
    public void setMetadata(EventMetadata eventMetadata) {

    }
}
