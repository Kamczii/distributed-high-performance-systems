package pl.rsww.offerread.offers.getting_offers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ShortInfoService {

    private final OfferShortInfoRepository shortInfoRepository;
    private final MongoTemplate mongoTemplate;

    public List<OfferShortInfo> search(GetOffers getOffers) {
        final var query = buildQuery(getOffers);
        return mongoTemplate.find(query, OfferShortInfo.class);
    }

    public OfferShortInfo getById(UUID offerId) {
        return shortInfoRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException(OfferShortInfo.class.getName(), offerId.toString()));
    }

    private static Query buildQuery(GetOffers getOffers) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (getOffers.persons() != null) {
            criteria.and("hotel.room.capacity").gte(getOffers.persons());
        }
        if (getOffers.kids() != null) {
//            criteria.and("kids").gt(getOffers.kids());
        }
        if (getOffers.destinationCity() != null) {
            criteria.and("destination.city").is(getOffers.destinationCity());
        }
        if (getOffers.destinationCountry() != null) {
            criteria.and("destination.country").is(getOffers.destinationCountry());
        }
        if (getOffers.departureCity() != null) {
            criteria.and("departure.city").is(getOffers.departureCity());
        }
        if (getOffers.departureCountry() != null) {
            criteria.and("departure.country").is(getOffers.departureCountry());
        }

        if (getOffers.startDate() != null && getOffers.endDate() != null) {
            criteria.and("start").gte(getOffers.startDate()).and("end").lte(getOffers.endDate());
        } else if (getOffers.startDate() != null && getOffers.endDate() == null) {
            criteria.and("start").is(getOffers.startDate());
        } else if (getOffers.endDate() != null && getOffers.startDate() == null) {
            criteria.and("end").is(getOffers.endDate());
        }

        if (getOffers.transport() != null) {
            criteria.and("transport").is(getOffers.transport());
        }

        query.addCriteria(criteria);

        if (getOffers.pageNumber() != null && getOffers.pageSize() != null) {
            Pageable pageable = PageRequest.of(getOffers.pageNumber(), getOffers.pageSize());
            query.with(pageable);
        }
        return query;
    }

}
