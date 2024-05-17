package pl.rsww.touroperator.hotels;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.rsww.tour_operator.api.HotelRequests;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.hotels.rooms.HotelRoom;
import pl.rsww.touroperator.initialization.EventSender;

import java.util.*;

@Slf4j
@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;
    private EventSender eventSender;

    private void sendRequest(Hotel hotel){
        UUID uuid = hotel.getId();
        HotelRequests.LocationRequest requestLoc = new HotelRequests.LocationRequest(hotel.getLocation().getCountry(), hotel.getLocation().getCity());
        List<HotelRequests.RoomRequest> roomRequests = new LinkedList<>();
        for(HotelRoom room: hotel.getRooms()){
            Set<HotelRequests.AgeRangePrice> priceListRequests = new HashSet<>();
            for(AgeRangePriceItem item: room.getPriceList()){
                priceListRequests.add(new HotelRequests.AgeRangePrice(item.getStartingRange(), item.getEndingRange(), item.getPrice()));
            }
            for(int i = 0; i < room.getNumberInHotel(); i++){
                roomRequests.add(new HotelRequests.RoomRequest(room.getDescription(), room.getMaxPeople(), room.getNumberOfBeds(), priceListRequests));
            }
        }

        HotelRequests.CreateHotel hotelRequest = new HotelRequests.CreateHotel(uuid, hotel.getName(), requestLoc, roomRequests);
        String key = uuid.toString();
        eventSender.sendHotel(hotelRequest, key);
    }

    @Async
    public void sendRequests() {
        log.info("Started sending hotels");
        eventSender = EventSender.getEventSender();
        Iterable<Hotel> hotels = hotelRepository.findAll();
        for(Hotel hotel: hotels){
            sendRequest(hotel);
        }
        log.info("Finished sending hotels");
    }
}
