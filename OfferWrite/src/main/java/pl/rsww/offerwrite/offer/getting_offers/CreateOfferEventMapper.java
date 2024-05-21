package pl.rsww.offerwrite.offer.getting_offers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.integration.AvailableOfferStatus;
import pl.rsww.offerwrite.api.integration.AvailableTransportType;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;
import pl.rsww.offerwrite.location.Location;
import pl.rsww.offerwrite.mapper.AgeRangePriceMapperImpl;
import pl.rsww.offerwrite.offer.getting_offers.strategy.context.OfferStrategyContext;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateOfferEventMapper {

    private final OfferRepository offerRepository;
    private final AgeRangePriceMapperImpl ageRangePriceMapper;
    private final OfferStrategyContext offerStrategyContext;

    public OfferIntegrationEvent.Created createOfferCreatedEvent(UUID offerId) {
        final var offer = fetchOffer(offerId);
        var departure = mapLocation(getInitialLocation(offer));
        var destination = mapLocation(getDestinationLocation(offer));
        var room = mapRoom(offer);
        var hotel = mapHotel(offer, room);
        var start = getStartDate(offer);
        var end = getEndDate(offer);
        var priceList = getPriceListForOffer(offer);
        var transport = getTransport(offer);
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Created(offer.getId(), hotel, departure, destination, start, end, AvailableOfferStatus.OPEN, transport, priceList);
    }

    private AvailableTransportType getTransport(Offer offer) {
        return offerStrategyContext.resolve(offer)
                .getTransport(offer);
    }

    private Location getInitialLocation(Offer offer) {
        return offerStrategyContext.resolve(offer)
                .getInitialLocation(offer);
    }

    private Location getDestinationLocation(Offer offer) {
        return offerStrategyContext.resolve(offer)
                                   .getDestinationLocation(offer);
    }

    private LocalDate getStartDate(Offer offer) {
        return offerStrategyContext.resolve(offer)
                                   .getStartDate(offer);
    }

    private LocalDate getEndDate(Offer offer) {
        return offerStrategyContext.resolve(offer)
                                   .getEndDate(offer);
    }

    private static pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location mapLocation(Location offer) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Location(offer.getCity(), offer.getCountry());
    }

    private static pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room mapRoom(Offer offer) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room(offer.getHotelRoom().getType(), offer.getHotelRoom().getCapacity(), offer.getHotelRoom().getBeds());
    }

    private static pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel mapHotel(Offer offer, pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Room room) {
        return new pl.rsww.offerwrite.api.integration.OfferIntegrationEvent.Hotel(offer.getHotelRoom().getHotel().getName(), room);
    }

    private Collection<OfferIntegrationEvent.AgeRangePrice> getPriceListForOffer(Offer offer) {
        final var priceList = offerStrategyContext.resolve(offer)
                .getPriceList(offer);
        return ageRangePriceMapper.map(priceList);
    }

    private Offer fetchOffer(UUID offerId) {
        return offerRepository.findById(offerId)
                              .orElseThrow(() -> new ResourceNotFoundException(Offer.class.toString(), offerId.toString()));
    }
}
