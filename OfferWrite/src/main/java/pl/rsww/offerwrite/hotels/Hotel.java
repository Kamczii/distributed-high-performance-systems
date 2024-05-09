package pl.rsww.offerwrite.hotels;

import lombok.Builder;
import lombok.Getter;
import pl.rsww.dominik.api.HotelRequests;
import pl.rsww.offerwrite.common.location.Location;
import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRooms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static pl.rsww.offerwrite.hotels.HotelEvent.HotelCreated;

@Getter
public class Hotel extends AbstractAggregate<HotelEvent, UUID> {
    public static final int LOCK_TIME_IN_SECONDS = 2*60;

    private HotelRooms rooms;
    private Location location;
    private String name;
    private Map<String, List<Set<ReservationDTO>>> activeReservations;

    Hotel() {
    }

    public static Hotel empty() {
        return new Hotel();
    }

    Hotel(
            UUID hotelId,
            String name,
            Location location,
            HotelRooms rooms
    ) {
        enqueue(new HotelCreated(hotelId, name, location, rooms));
    }

    public static Hotel create(HotelRequests.CreateHotel create) {
        var location = new Location(create.location().country(), create.location().city());
        var rooms = create.rooms()
                .stream()
                .map(roomRequest -> new HotelRoom(roomRequest.type(), roomRequest.capacity(), roomRequest.beds()))
                .collect(Collectors.collectingAndThen(toList(), HotelRooms::new));
        return new Hotel(create.hotelId(), create.name(), location, rooms);
    }

    @Override
    public void when(HotelEvent event) {
        switch (event) {
            case HotelCreated hotelCreated -> {
                id = hotelCreated.hotelId();
                location = hotelCreated.location();
                name = hotelCreated.name();
                rooms = hotelCreated.rooms();
                activeReservations = hotelCreated.rooms().rooms().stream().collect(
                        Collectors.groupingBy(HotelRoom::type, Collectors.mapping(
                                _ -> new HashSet<>(),
                                toList()
                        ))
                );
            }
            case HotelEvent.RoomReserved roomReserved -> {
                if (onGoingReservation(roomReserved)) {
                    getAvailableRoom(roomReserved.type(), roomReserved.checkInDate(), roomReserved.checkOutDate())
                            .ifPresent(set -> set.add(new ReservationDTO(
                                    roomReserved.type(),
                                    roomReserved.orderId(),
                                    roomReserved.checkInDate(),
                                    roomReserved.checkOutDate())));
                }
            }
            case HotelEvent.RoomConfirmed roomConfirmed -> {
                final var shouldAddReservation = orderDoesntExist(roomConfirmed);
                if (shouldAddReservation) {
                    getAvailableRoom(roomConfirmed.type(), roomConfirmed.checkInDate(), roomConfirmed.checkOutDate())
                            .ifPresent(set -> set.add(new ReservationDTO(
                                    roomConfirmed.type(),
                                    roomConfirmed.orderId(),
                                    roomConfirmed.checkInDate(),
                                    roomConfirmed.checkOutDate())));
                }
            }
        }
    }

    private boolean orderDoesntExist(HotelEvent.RoomConfirmed roomConfirmed) {
        return findOrder(roomConfirmed.orderId()).isEmpty();
    }

    private List<Set<ReservationDTO>> getReservationsForRoom(String roomType) {
        return activeReservations.get(roomType);
    }

    private static boolean onGoingReservation(HotelEvent.RoomReserved roomReserved) {
        return roomReserved.time().isAfter(LocalDateTime.now().minusSeconds(LOCK_TIME_IN_SECONDS));
    }

    static String mapToStreamId(UUID hotelId) {
        return "Hotel-%s".formatted(hotelId);
    }

    public void lock(UUID orderId, String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        if (!roomAvailable(roomType, checkInDate, checkOutDate))
            throw new IllegalStateException("No available room for this type");
        enqueue(new HotelEvent.RoomReserved(orderId, roomType, LocalDateTime.now(), checkInDate, checkOutDate));
    }

    private boolean roomAvailable(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        return getReservationsForRoom(roomType)
                .stream()
                .anyMatch(roomReservations -> available(roomReservations, checkInDate, checkOutDate));
    }

    private Optional<Set<ReservationDTO>> getAvailableRoom(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        return getReservationsForRoom(roomType)
                .stream()
                .filter(roomReservations -> available(roomReservations, checkInDate, checkOutDate))
                .findAny();
    }

    private Optional<ReservationDTO> findOrder(UUID orderId) {
        return activeReservations.values()
                .stream()
                .flatMap(List::stream)
                .flatMap(Set::stream)
                .filter(reservationDTO -> reservationDTO.orderId().equals(orderId))
                .findAny();
    }


    public void confirmLock(UUID orderId) {
        findOrder(orderId).ifPresentOrElse(reservation ->
                        enqueue(new HotelEvent.RoomConfirmed(orderId, reservation.roomType(), reservation.checkInDate(), reservation.checkOutDate())),
                () -> {
                    throw new IllegalStateException("Reservation not found");
                });
    }

    public BigDecimal getPrice(String roomType, Integer age) {
        if (age < 18) {
            return BigDecimal.ONE;
        }
        return BigDecimal.TEN;
    }


    public record ReservationDTO (
        String roomType,
        UUID orderId,
        LocalDate checkInDate,
        LocalDate checkOutDate
    ) {
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            ReservationDTO that = (ReservationDTO) object;
            return Objects.equals(orderId, that.orderId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderId);
        }
    }



    private boolean available(Set<ReservationDTO> reservations, LocalDate checkInDate, LocalDate checkOutDate) {
        return reservations.stream()
                .noneMatch(reservation -> isOverlap(checkInDate, checkOutDate, reservation.checkInDate(), reservation.checkOutDate()));
    }

    boolean isOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !(end1.isBefore(start2) || end1.equals(start2) || start1.isAfter(end2) || start1.equals(end2));
    }

}
