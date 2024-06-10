package pl.rsww.offerwrite.hotels;

import lombok.Getter;
import pl.rsww.tour_operator.api.HotelRequests;
import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;
import pl.rsww.offerwrite.common.location.Location;
import pl.rsww.offerwrite.core.aggregates.AbstractAggregate;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRoom;
import pl.rsww.offerwrite.hotels.hotel_rooms.HotelRooms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;
import static pl.rsww.offerwrite.constants.Constants.LOCK_TIME_IN_SECONDS;
import static pl.rsww.offerwrite.hotels.HotelEvent.HotelCreated;

@Getter
public class Hotel extends AbstractAggregate<HotelEvent, UUID> {
;

    private HotelRooms rooms;
    private Location location;
    private String name;
    private Map<String, List<ReservationDTO>> activeReservations;

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
        final var counts = create.rooms().stream().collect(Collectors.groupingBy(HotelRequests.RoomRequest::type, counting()));
        //ugly prevent rooms with different price
        final var collect = create.rooms().stream().collect(Collectors.groupingBy(HotelRequests.RoomRequest::type, Collectors.mapping(HotelRequests.RoomRequest::priceList, toList())));
        var rooms = create.rooms()
                .stream()
                .map(roomRequest -> new HotelRoom(roomRequest.type(), roomRequest.capacity(), roomRequest.beds(), counts.get(roomRequest.type()), new ArrayList<>()))
                .distinct()
                .map(hotelroom -> new HotelRoom(hotelroom.type(), hotelroom.capacity(), hotelroom.beds(), counts.get(hotelroom.type()), collect.get(hotelroom.type()).stream().findFirst().map(s -> s.stream().map(price -> new AgeRangePrice(price.startingRange(), price.endingRange(), price.price())).toList()).orElseThrow()))
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
                activeReservations = hotelCreated.rooms().rooms().stream().map(HotelRoom::type).distinct().collect(
                        Collectors.toMap(
                                key -> key,
                                _ -> new ArrayList<>()
                        )
                );
            }
            case HotelEvent.RoomReserved roomReserved -> {
                if (onGoingReservation(roomReserved)) {
                    getRoomReservations(roomReserved.type(), roomReserved.checkInDate(), roomReserved.checkOutDate())
                            .ifPresent(reservations -> reservations.add(new ReservationDTO(
                                    roomReserved.type(),
                                    roomReserved.orderId(),
                                    roomReserved.checkInDate(),
                                    roomReserved.checkOutDate())));
                }
            }
            case HotelEvent.RoomConfirmed roomConfirmed -> {
                final var shouldAddReservation = orderDoesntExist(roomConfirmed);
                if (shouldAddReservation) {
                    getRoomReservations(roomConfirmed.type(), roomConfirmed.checkInDate(), roomConfirmed.checkOutDate())
                            .ifPresent(reservations -> reservations.add(new ReservationDTO(
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

    private List<ReservationDTO> getReservationsForRoom(String roomType) {
        return activeReservations.get(roomType);
    }

    private boolean onGoingReservation(HotelEvent.RoomReserved roomReserved) {
        return roomReserved.time().isAfter(LocalDateTime.now().minusSeconds(LOCK_TIME_IN_SECONDS));
    }

    static String mapToStreamId(UUID hotelId) {
        return "Hotel-%s".formatted(hotelId);
    }

    public void lock(UUID orderId, String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        if (!roomAvailable(roomType, checkInDate, checkOutDate))
            throw new IllegalStateException("No available room for this type");
        enqueue(new HotelEvent.RoomReserved(id(), orderId, roomType, LocalDateTime.now(), checkInDate, checkOutDate));
    }

    public boolean roomAvailable(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        final var roomsOfType = countRoomsOfType(roomType);
        return getReservationsForRoom(roomType)
                .stream()
                .filter(reservation -> isOverlap(reservation.checkInDate(), reservation.checkOutDate(), checkInDate, checkOutDate))
                .count() < roomsOfType;
    }

    private long countRoomsOfType(String roomType) {
        return this.rooms.rooms().stream().filter(room -> room.type().equals(roomType)).map(HotelRoom::count).reduce(0L, Long::sum);
    }

    private Optional<List<ReservationDTO>> getRoomReservations(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        if (roomAvailable(roomType, checkInDate, checkOutDate)) {
            return Optional.ofNullable(getReservationsForRoom(roomType));
        } else {
            return Optional.empty();
        }
    }

    private Optional<ReservationDTO> findOrder(UUID orderId) {
        return activeReservations.values()
                .stream()
                .flatMap(List::stream)
                .filter(reservationDTO -> reservationDTO.orderId().equals(orderId))
                .findAny();
    }


    public void confirmLock(UUID orderId) {
        findOrder(orderId).ifPresentOrElse(reservation ->
                        enqueue(new HotelEvent.RoomConfirmed(id(), orderId, reservation.roomType(), reservation.checkInDate(), reservation.checkOutDate())),
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
    boolean isOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !(end1.isBefore(start2) || end1.equals(start2) || start1.isAfter(end2) || start1.equals(end2));
    }

}
