package pl.rsww.offerwrite.buses;

import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;
import pl.rsww.offerwrite.common.location.Location;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public sealed interface BusEvent {
    record BusCreated(Location departure,
                         Location destination,
                         String busNumber,
                         LocalDate date,
                         Integer capacity,
                         Collection<AgeRangePrice> priceList) implements BusEvent {}

    record SeatReserved(String busNumber, LocalDate date, Integer seats, LocalDateTime time, UUID orderId) implements BusEvent {}
    record SeatReleased(String busNumber, LocalDate date, Integer seats, UUID orderId) implements BusEvent {}
    record SeatReservedConfirmed(String busNumber, LocalDate date, Integer seats, UUID orderId) implements BusEvent {}
    record SeatConfirmationCanceled(String busNumber, LocalDate date, Integer seats, UUID orderId) implements BusEvent {}
}
