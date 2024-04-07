package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.api.requests.OfferRequests.OfferCreated;
import pl.rsww.offerwrite.flights.FlightEvent;
import pl.rsww.offerwrite.hotels.HotelEvent;

import java.util.Collection;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class OfferService {

    public Collection<OfferCreated> createOffers(FlightEvent.FlightCreated flightCreated) {
        return Collections.emptyList();
    }

    public Collection<OfferCreated> createOffers(HotelEvent.HotelCreated hotelCreated) {
        return Collections.emptyList();
    }
}
