package pl.rsww.offerread.offers.getting_offers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;


public record GetOffers(Integer pageNumber, Integer pageSize, Integer persons, Collection<LocalDate> kids,
                        String name, String departureCity, String departureCountry, String destinationCity, String destinationCountry, LocalDate startDate, LocalDate endDate, String transport) {
  public GetOffers {
    if (pageNumber != null && pageNumber < 0)
      throw new IllegalArgumentException("Page number has to be a zero-based number");

    if (pageSize != null && pageSize < 0)
      throw new IllegalArgumentException("Page size has to be a zero-based number");
  }


  public static Page<OfferShortInfo> handle(
          OfferShortInfoRepository repository,
          GetOffers query
  ) {
    return repository.findAll(
            PageRequest.of(query.pageNumber(), query.pageSize())
    );
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (GetOffers) obj;
    return this.pageNumber == that.pageNumber &&
            this.pageSize == that.pageSize &&
            Objects.equals(this.persons, that.persons) &&
            Objects.equals(this.kids, that.kids) &&
            Objects.equals(this.destinationCity, that.destinationCity) &&
            Objects.equals(this.startDate, that.startDate) &&
            Objects.equals(this.endDate, that.endDate) &&
            Objects.equals(this.transport, that.transport);
  }

  @Override
  public String toString() {
    return "GetOffers[" +
            "pageNumber=" + pageNumber + ", " +
            "pageSize=" + pageSize + ", " +
            "persons=" + persons + ", " +
            "kids=" + kids + ", " +
            "destinationCity=" + destinationCity + ", " +
            "startDate=" + startDate + ", " +
            "endDate=" + endDate + ", " +
            "transport=" + transport + ']';
  }

}
