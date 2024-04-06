package pl.rsww.offerread.offers.getting_offers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import pl.rsww.offerread.projections.MongoProjection;

import java.util.UUID;

@Component
public class OfferShortInfoProjection extends MongoProjection<OfferShortInfo, UUID> {
    public OfferShortInfoProjection(MongoRepository<OfferShortInfo, UUID> repository) {
        super(repository);
    }
}
