package pl.rsww.offerread.offers.getting_offers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public record GetOffers(
  int pageNumber,
  int pageSize,
  Integer persons,
  Integer kids,
  String from,
  String destination,
  LocalDate startDate,
  LocalDate endDate,
  String transport
) {
  public GetOffers {
    if (pageNumber < 0)
      throw new IllegalArgumentException("Page number has to be a zero-based number");

    if (pageSize < 0)
      throw new IllegalArgumentException("Page size has to be a zero-based number");
  }

//  public static GetOffers of(@Nullable Integer pageNumber, @Nullable Integer pageSize) {
//
//    return new GetOffers(
//      pageNumber != null ? pageNumber : 0,
//      pageSize != null ? pageSize : 20
//    );
//  }

  public static Page<OfferShortInfo> handle(
          OfferShortInfoRepository repository,
          GetOffers query
  ) {
    return repository.findAll(
      PageRequest.of(query.pageNumber(), query.pageSize())
    );
  }
}
