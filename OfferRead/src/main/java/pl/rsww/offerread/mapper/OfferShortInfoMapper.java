package pl.rsww.offerread.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfo;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfoDTO;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OfferShortInfoMapper {
   @Mapping(target = "id", source = "source.offerId")
   OfferShortInfoDTO map(OfferShortInfo source, BigDecimal price);

}
