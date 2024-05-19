package pl.rsww.offerwrite.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pl.rsww.offerwrite.flights.FlightCommand;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.flights.getting_flight_seats.Flight;
import pl.rsww.offerwrite.hotels.HotelCommand;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Slf4j

@Service
@RequiredArgsConstructor
public class OfferService {

    private final FlightService flightService;
    private final OfferRepository offerRepository;
    private final HotelService hotelService;

    public void reserveOffer(UUID offerId, UUID orderId, Collection<Integer> ageOfVisitors) {
        final var offer = fetchOffer(offerId);
        final var capacity = offer.getHotelRoom().getCapacity();

        final var visitorsCount = ageOfVisitors.size();
        if (visitorsCount > capacity) {
            throw new IllegalStateException(String.format("The number of people exceeds the capacity, required %d, got %d. (Offer %s, Order %s)", visitorsCount, capacity, offerId, orderId));
        }

        ArrayList<FlightCommand> rollbacks = new ArrayList<>();

        try {
            final var initialFlightRequest = buildFlightLockRequest(orderId, offer.getInitialFlight(), capacity);
            flightService.handle(initialFlightRequest);
            rollbacks.add(buildFlightLockReleaseRequest(orderId, offer.getInitialFlight(), capacity));

            final var returnFlightRequest = buildFlightLockRequest(orderId, offer.getReturnFlight(), capacity);
            flightService.handle(returnFlightRequest);
            rollbacks.add(buildFlightLockReleaseRequest(orderId, offer.getReturnFlight(), capacity));

            final var hotelLockRequest = buildHotelLockRequest(orderId, offer);
            hotelService.handle(hotelLockRequest);
            log.info(String.format("Successful lock (Offer %s, Order %s)", offerId, orderId));
        } catch (Exception e) {
            rollbacks.forEach(this::handle);
            throw new IllegalStateException(String.format("Failed to lock (Offer %s, Order %s), reason: %s", offerId, orderId, e.getMessage()));
        }


    }

    private static FlightCommand.Lock buildFlightLockRequest(UUID orderId, Flight flight, Integer capacity) {
        return new FlightCommand.Lock(flight.getFlightNumber(), capacity, orderId, flight.getDate());
    }

    private static FlightCommand.ReleaseLock buildFlightLockReleaseRequest(UUID orderId, Flight flight, Integer capacity) {
        return new FlightCommand.ReleaseLock(flight.getFlightNumber(), capacity, orderId, flight.getDate());
    }

    private static FlightCommand.CancelConfirmation buildFlightCancelConfirmationRequest(UUID orderId, Flight flight, Integer capacity) {
        return new FlightCommand.CancelConfirmation(flight.getFlightNumber(), capacity, orderId, flight.getDate());
    }

    public void confirmOffer(UUID offerId, UUID orderId) {
        final var offer = fetchOffer(offerId);
        final var capacity = offer.getHotelRoom().getCapacity();
        ArrayList<FlightCommand> flightRollbacks = new ArrayList<>();
        try {
            final var initialFlightRequest = buildFlightConfirmLockRequest(orderId, offer.getInitialFlight(), capacity);
            flightService.handle(initialFlightRequest);
            flightRollbacks.add(buildFlightCancelConfirmationRequest(orderId, offer.getInitialFlight(), capacity));

            final var returnFlightRequest = buildFlightConfirmLockRequest(orderId, offer.getReturnFlight(), capacity);
            flightService.handle(returnFlightRequest);
            flightRollbacks.add(buildFlightCancelConfirmationRequest(orderId, offer.getInitialFlight(), capacity));

            final var hotelConfirmLockRequest = buildHotelConfirmLockRequest(orderId, offer);
            hotelService.handle(hotelConfirmLockRequest);
        } catch (Exception e) {
            flightRollbacks.forEach(this::handle);
            throw new IllegalStateException("Failed to confirm offer");
        }
    }

    private HotelCommand.ConfirmLock buildHotelConfirmLockRequest(UUID orderId, Offer offer) {
        return new HotelCommand.ConfirmLock(offer.getHotelRoom().getHotel().getId(), orderId);
    }

    private static HotelCommand.Lock buildHotelLockRequest(UUID orderId, Offer offer) {
        return new HotelCommand.Lock(offer.getHotelRoom().getHotel().getId(),
                orderId,
                offer.getHotelRoom().getType(),
                offer.getInitialFlight().getDate(),
                offer.getReturnFlight().getDate());
    }

    private void handle(FlightCommand command) {
        try {
            flightService.handle(command);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void handle(HotelCommand command) {
        try {
            hotelService.handle(command);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static FlightCommand.ConfirmLock buildFlightConfirmLockRequest(UUID orderId, Flight flight, Integer capacity) {
        return new FlightCommand.ConfirmLock(flight.getFlightNumber(), capacity, orderId, flight.getDate());
    }


    private Offer fetchOffer(UUID offerId) {
        return offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException(Offer.class.toString(), offerId.toString()));
    }

}
