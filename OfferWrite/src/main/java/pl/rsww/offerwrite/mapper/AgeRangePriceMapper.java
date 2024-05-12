package pl.rsww.offerwrite.mapper;

import org.mapstruct.Mapper;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface AgeRangePriceMapper {
   Collection<OfferIntegrationEvent.AgeRangePrice> map(Collection<AgeRangePrice> source);

}
