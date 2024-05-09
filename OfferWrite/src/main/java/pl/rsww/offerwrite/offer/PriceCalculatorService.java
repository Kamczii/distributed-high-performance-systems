package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.flights.Flight;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.hotels.Hotel;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PriceCalculatorService {
    private final OfferRepository offerRepository;
    private final FlightService flightService;
    private final HotelService hotelService;

    public BigDecimal calculate(UUID offerId, Collection<Integer> ageOfVisitors) {
        final var offer = fetchOffer(offerId);
        final var tripDuration = getTripDuration(offer);

        final var hotel = getHotel(offer);
        final var component1 = calculateTotalPrice(ageOfVisitors, age -> hotel.getPrice(offer.getHotelRoom().getType(), age))
                .multiply(BigDecimal.valueOf(tripDuration));

        final var initialFlight = getFlight(offer.getInitialFlight());
        final var component2 = calculateTotalPrice(ageOfVisitors, initialFlight::getPrice);

        final var returnFlight = getFlight(offer.getReturnFlight());
        final var component3 = calculateTotalPrice(ageOfVisitors, returnFlight::getPrice);

        return component1.add(component2).add(component3);
    }

    private BigDecimal calculateTotalPrice(Collection<Integer> ageOfVisitors, Function<Integer, BigDecimal> priceFunction) {
        return ageOfVisitors.stream()
                .map(priceFunction)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Flight getFlight(pl.rsww.offerwrite.flights.getting_flight_seats.Flight flight) {
        final var initialFlightId = flight.getFlightId();
        return flightService.get(initialFlightId);
    }

    private Hotel getHotel(Offer offer) {
        final var hotelId = offer.getHotelRoom().getHotel().getId();
        return hotelService.getEntity(hotelId);
    }

    private static long getTripDuration(Offer offer) {
        return ChronoUnit.DAYS.between(offer.getInitialFlight().getDate(), offer.getReturnFlight().getDate());
    }

    private Offer fetchOffer(UUID offerId) {
        return offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException(Offer.class.toString(), offerId.toString()));
    }
}
