package pl.rsww.offerwrite.api;

import java.time.LocalDate;
import java.util.UUID;

public sealed interface OfferIntegrationEvent {
    record Created(UUID offerId,
                   Hotel hotel,
                   Location departure,
                   Location destination,
                   LocalDate start,
                   LocalDate end,
                   AvailableOrderStatus status
    ) implements OfferIntegrationEvent {}

    record Hotel (String name, Room room) {

    }

    record Room(String type, Integer capacity, Integer beds) {

    }

    record Location(String city, String country) {

    }
}