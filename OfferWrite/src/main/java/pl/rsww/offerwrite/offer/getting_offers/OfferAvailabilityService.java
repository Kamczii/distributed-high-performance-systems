package pl.rsww.offerwrite.offer.getting_offers;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.hotels.Hotel;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.offer.getting_offers.strategy.context.impl.OfferStrategyContextImpl;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferAvailabilityService {


    private final OfferRepository offerRepository;
    private final HotelService hotelService;
    private final OfferStrategyContextImpl offerStrategyContext;

    public boolean isOfferAvailable(UUID offerId) {
        final var offer = fetchOffer(offerId);
        final var hotelId = offer.getHotelRoom().getHotel().getId();
        final var roomType = offer.getHotelRoom().getType();
        final var hotelCurrentState = getHotelCurrentState(hotelId);
        final var availableSeats = availableSeats(offer);
        return hotelCurrentState.getRooms().rooms().stream().anyMatch(room ->
                roomType.equals(room.type()) && availableSeats >= room.capacity()
        );
    }

    private int availableSeats(Offer offer) {
        return offerStrategyContext.resolve(offer)
                .getAvailableSeats(offer);
    }

    private Hotel getHotelCurrentState(UUID hotelId) {
        return hotelService.getEntity(hotelId);
    }

    private Offer fetchOffer(UUID offerId) {
        return offerRepository.findById(offerId)
                              .orElseThrow(() -> new ResourceNotFoundException(Offer.class.toString(), offerId.toString()));
    }
}
