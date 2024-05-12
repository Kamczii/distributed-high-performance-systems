package pl.rsww.offerread.mapper;

import org.mapstruct.Mapper;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfo;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface AgeRangePriceMapper {
   Collection<OfferShortInfo.AgeRangePrice> map(Collection<OfferIntegrationEvent.AgeRangePrice> source);

}
