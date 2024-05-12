package pl.rsww.offerread.mapper;

import org.mapstruct.Mapper;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfo;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfoDTO;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;

import java.math.BigDecimal;
import java.util.Collection;

@Mapper(componentModel = "spring")
public interface OfferShortInfoMapper {
   OfferShortInfoDTO map(OfferShortInfo source, BigDecimal price);

}
