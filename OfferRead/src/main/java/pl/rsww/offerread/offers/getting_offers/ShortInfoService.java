package pl.rsww.offerread.offers.getting_offers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.rsww.offerread.mapper.OfferShortInfoMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class ShortInfoService {

    private final OfferShortInfoRepository shortInfoRepository;
    private final MongoTemplate mongoTemplate;
    private final OfferShortInfoMapper offerShortInfoMapper;
    public List<OfferShortInfoDTO> search(GetOffers getOffers) {
        final var query = buildQuery(getOffers);
        final var adults = getAdultsCount(getOffers);
        return mongoTemplate.find(query, OfferShortInfo.class)
                .stream()
                .map(offer -> map(offer, adults, getOffers.kids()))
                .toList();
    }

    private static Integer getAdultsCount(GetOffers getOffers) {
        return Optional.ofNullable(getOffers.persons())
                .map(persons -> persons - Optional.ofNullable(getOffers.kids()).map(Collection::size).orElse(0))
                .orElse(1);
    }

    private OfferShortInfoDTO map(OfferShortInfo source, Integer adults, Collection<LocalDate> kids) {
        final var ageOfVisitors = Stream.concat(adultStream(adults),
                childStream(kids)).toList();
        final var price = getPrice(source, ageOfVisitors);
        return offerShortInfoMapper.map(source, price);
    }

    private BigDecimal getPrice(OfferShortInfo offer, Collection<Integer> ageOfVisitors) {
        return ageOfVisitors.stream()
                .map(offer::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Stream<Integer> childStream(Collection<LocalDate> kids) {
        return Optional.ofNullable(kids).orElse(Collections.emptyList())
                .stream().map(this::calculateAge);
    }

    private static Stream<Integer> adultStream(Integer persons) {
        return Stream.generate(() -> 18).limit(persons != null ? persons : 1);
    }

    public Integer calculateAge(LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
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
