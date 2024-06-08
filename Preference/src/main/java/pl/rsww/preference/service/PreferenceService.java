package pl.rsww.preference.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rsww.preference.api.PreferenceEvent;
import pl.rsww.preference.api.PreferenceTopics;
import pl.rsww.preference.model.Destination;
import pl.rsww.preference.model.Hotel;
import pl.rsww.preference.model.Room;
import pl.rsww.preference.publisher.KafkaPublisher;
import pl.rsww.preference.repository.DestinationRepository;
import pl.rsww.preference.repository.HotelRepository;
import pl.rsww.preference.repository.RoomRepository;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;

@Service
@RequiredArgsConstructor
public class PreferenceService {
    private final DestinationRepository destinationRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    private final KafkaPublisher kafkaPublisher;

    @Transactional
    public void addDestination(String country, String city) {
        Destination destination = destinationRepository.findByCountryAndCity(country, city).orElse(null);

        if (destination == null) {
            Destination destinationNew = Destination.builder().country(country).city(city).build();
            destinationRepository.save(destinationNew);
        } else {
            destination.setOccurrences(destination.getOccurrences() + 1);
            destinationRepository.save(destination);
        }
    }

    @Transactional
    public void addHotelAndRoom(String hotelName, String roomType, Integer roomCapacity, Integer roomBeds) {
        Room room = roomRepository.findByTypeAndCapacityAndBeds(roomType, roomCapacity, roomBeds).orElse(null);
        Hotel hotel = hotelRepository.findByNameAndRoom(hotelName, room).orElse(null);
        if (hotel != null) {
            hotel.setOccurrences(hotel.getOccurrences() + 1);
        } else {
            Room roomNew = Room.builder().type(roomType).capacity(roomCapacity).beds(roomBeds).build();
            roomNew = roomRepository.save(roomNew);

            Hotel hotelNew = Hotel.builder().name(hotelName).room(room == null ? roomNew : room).build();
            hotelRepository.save(hotelNew);
        }

    }

    @Scheduled(fixedRate = 2000)
    public void publishMostPopularEntities() {
        publishDestinationPreference();
        publishHotelPreference();
    }

    public void publishDestinationPreference() {
        destinationRepository.findPreferenceDestination().ifPresent(destination -> {
            PreferenceEvent.Destination event = new PreferenceEvent.Destination(destination.getCountry(), destination.getCity());
            kafkaPublisher.publish(PreferenceTopics.PREFERENCE_BASIC_TOPIC, event);
        });
    }

    public void publishHotelPreference() {
        hotelRepository.findPreferenceHotel().ifPresent(hotel -> {
            Room room = hotel.getRoom();
            PreferenceEvent.Hotel event = new PreferenceEvent.Hotel(hotel.getName(), new OfferIntegrationEvent.Room(room.getType(), room.getCapacity(), room.getBeds()));
            kafkaPublisher.publish(PreferenceTopics.PREFERENCE_BASIC_TOPIC, event);
        });
    }
}
