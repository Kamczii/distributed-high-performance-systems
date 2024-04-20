package pl.rsww.touroperator.hotels;


import pl.rsww.touroperator.hotels.rooms.HotelRoom;
import pl.rsww.touroperator.data.StringNumberPair;
import pl.rsww.touroperator.hotels.reservations.RoomReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(path="/hotels")
public class HotelController {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomReservationRepository roomReservationRepository;


    @GetMapping(path="/all")
    public @ResponseBody Iterable<String> getAllHotels() {
        List<String> results = new LinkedList<>();
        for(Hotel h: hotelRepository.findAll()){
            results.add(h.getName());
        }
        return results;
    }

    @GetMapping(path="/list-rooms")
    public @ResponseBody Iterable<String> listRoomsOf(@RequestParam String hotelName) {
        Hotel hotel = hotelRepository.findByName(hotelName);
        List<String> results = new LinkedList<>();
        for(HotelRoom room: hotel.getRooms()){
            results.add(room.getDescription());
        }
        return results;
    }

    @GetMapping(path="/list-free-rooms")
    public @ResponseBody List<StringNumberPair> listFreeRoomsOfHotel(@RequestParam String hotelName) {
        Hotel hotel = hotelRepository.findByName(hotelName);

        Iterator<HotelRoom> it = hotel.getRooms().iterator();
        List<StringNumberPair> results = new LinkedList<>();

        while (it.hasNext()){
            HotelRoom room = it.next();
//            Iterable<RoomReservation> reservations = roomReservationRepository.findAllByRoom(room);
            long numOfReservations = roomReservationRepository.countByRoom(room);
            StringNumberPair p = new StringNumberPair(room.getDescription(), room.getNumberInHotel() - numOfReservations);
            results.add(p);
        }
        return results;
    }
}