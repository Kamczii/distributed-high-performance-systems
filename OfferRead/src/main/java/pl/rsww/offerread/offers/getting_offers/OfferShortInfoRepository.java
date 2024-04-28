package pl.rsww.offerread.offers.getting_offers;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface OfferShortInfoRepository extends MongoRepository<OfferShortInfo, UUID> {

}
