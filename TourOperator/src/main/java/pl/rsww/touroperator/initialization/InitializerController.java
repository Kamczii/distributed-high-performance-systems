package pl.rsww.touroperator.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import pl.rsww.touroperator.data_extraction.HotelInfo;
import pl.rsww.touroperator.data_extraction.JsonDataExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.rsww.touroperator.flights.FlightRepository;
import pl.rsww.touroperator.flights.bookings.FlightBookingRepository;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.hotels.HotelRepository;
import pl.rsww.touroperator.hotels.reservations.RoomReservationRepository;
import pl.rsww.touroperator.hotels.rooms.HotelRoomRepository;
import pl.rsww.touroperator.locations.AirportLocationRepository;

import java.util.*;

@Controller
public class InitializerController {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private HotelRoomRepository hotelRoomRepository;
    @Autowired
    private RoomReservationRepository roomReservationRepository;
    @Autowired
    private FlightLineRepository flightLineRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportLocationRepository airportLocationRepository;
    @Autowired
    private FlightBookingRepository flightBookingRepository;

    @GetMapping(path="/init")
    public @ResponseBody String initialize() {

        JsonDataExtractor extractor = new JsonDataExtractor();
        List<HotelInfo> hotelInfoList = extractor.extract();
        HotelInitializer initializer = new HotelInitializer();
        initializer.setFlightBookingRepository(flightBookingRepository);
        initializer.setFlightLineRepository(flightLineRepository);
        initializer.setFlightRepository(flightRepository);
        initializer.setHotelRepository(hotelRepository);
        initializer.setHotelRoomRepository(hotelRoomRepository);
        initializer.setRoomReservationRepository(roomReservationRepository);
        initializer.setTouristicLocationRepository(airportLocationRepository);

        EventSender eventSender = new EventSender();
        initializer.setEventSender(eventSender);
        if(!initializer.checkIfOk()){
            return "Error: repositories are not wired to initializer";
        }

        for(HotelInfo info: hotelInfoList){
            initializer.initialize(info);
            initializer.clear();
        }

        return "initialized";
    }

}


//    @GetMapping(path="/init-reserv")
//    public @ResponseBody String initializeReservations() {
////        Date currentDate = new Date();
////
////        RoomReservation reservation = new RoomReservation();
////        Iterator<Hotel> it = hotelRepository.findAll().iterator();
////        Hotel h = it.next();
////        reservation.setRoom(h.getRooms().get(0));
////        reservation.setNumberOfGuests(2);
////        reservation.setNumberOfDays(7);
////        reservation.setReservationDate(currentDate);
////        roomReservationRepository.save(reservation);
////
////        FlightBooking booking = new FlightBooking();
////        booking.setReservation(reservation);
////        booking.setNumberOfPeople(2);
////        Optional<Flight> flight = flightRepository.findById(1);
////        booking.setFlight(flight.get());
////        flightBookingRepository.save(booking);
//
//        return "initialized";
//    }



//    private void initializeHotels(){
//        String[] hotel_names = {"aaa", "bbb"};
//        Region r = new Region();
//        r.setCountry("A");
//        r.setRegion("A");
//        regionRepository.save(r);
//        TouristicLocation location = new TouristicLocation();
//        location.setRegion(r);
//        location.setCity("A");
//        touristicLocationRepository.save(location);
//
//        for(int i = 0; i < 2; i++){
//            Hotel h = new Hotel();
//            h.setName(hotel_names[i]);
//            h.setLocation(location);
//            hotelRepository.save(h);
//            LinkedList<HotelRoom> rooms = new LinkedList<>();
//            HotelRoom room = new HotelRoom();
//            room.setHotel(h);
//            room.setDescription("Normalny pokój");
//            room.setMaxPeople(2);
//            room.setNumberInHotel(5);
//            hotelRoomRepository.save(room);
//            rooms.add(room);
//            h.setRooms(rooms);
//            hotelRepository.save(h);
//        }
//        List<Hotel> hotels_list = (List<Hotel>) hotelRepository.findAll();
//        location.setHotels(hotels_list);
//        touristicLocationRepository.save(location);
//
//
//        FlightLine line = new FlightLine();
//        line.setDestinationAirport("A");
//        line.setHomeAirport("Home");
//        line.setMaxPassengers(10);
//        line.setLocation(location);
//        flightLineRepository.save(line);
//
//        for (Hotel h : hotels_list) {
//            Set<FlightLine> fls = new HashSet<>();
//            fls.add(line);
//            h.setFlightLines(fls);
//            hotelRepository.save(h);
//        }
//
//        Flight flight = new Flight();
//        flight.setLine(line);
//        flight.setDepartureDate(new Date());
//        flightRepository.save(flight);
//    }

//    private void initializeFlights(){
//        FlightLine line = new FlightLine();
//        line.setDestinationAirport("A");
//        line.setHomeAirport("Home");
//        line.setMaxPassengers(10);
//        line.setHotels((List)hotelRepository.findAll());
//        flightLineRepository.save(line);
//
//        Flight flight = new Flight();
//        flight.setLine(line);
//        flight.setDepartureDate(new Date());
//        flightRepository.save(flight);
//    }

//    private void aaa(List<HotelInfo> hotelInfoList){
//        for(HotelInfo info: hotelInfoList){
//            Hotel hotel = new Hotel();
//            hotel.setName(info.getName());
//            Region region = regionRepository.findByRegion(info.getRegion());
//            if(region == null){
//                region = new Region();
//                region.setCountry(info.getCountry());
//                region.setRegion(info.getRegion());
//                regionRepository.save(region);
//            }
//            TouristicLocation location = touristicLocationRepository.findByCity(info.getCity());
//            if(location == null){
//                location = new TouristicLocation();
//                location.setRegion(region);
//                location.setCity(info.getCity());
//                location.setHotels(new LinkedList<>());
//                touristicLocationRepository.save(location);
//            }
//            hotelRepository.save(hotel);
//            location.addHotel(hotel);
//            touristicLocationRepository.save(location);
//            hotel.setLocation(location);
//            List<HotelRoom> rooms = new LinkedList<>();
//            for(String roomDesc: info.getRooms()){
//                HotelRoom r = new HotelRoom();
//                r.setHotel(hotel);
//                r.setDescription(roomDesc);
//                r.setNumberInHotel(1);
//                r.setMaxPeople(2);
//                hotelRoomRepository.save(r);
//                rooms.add(r);
//            }
//            hotel.setRooms(rooms);
//            hotelRepository.save(hotel);
//
//            hotel.setFlightLines(new HashSet<>());
//            List<String> departures = info.getDepartures();
//            for(String departure: departures){
//                FlightLine fl = flightLineRepository.findByHomeAirportAndDestinationAirport(departure, info.getCity());
//                if(fl == null){
//                    fl = new FlightLine();
//                    fl.setLocation(location);
//                    fl.setHotels(new HashSet<>());
//                    fl.setHomeAirport(departure);
//                    fl.setDestinationAirport(info.getCity());
//                    fl.setMaxPassengers(50);
//                    flightLineRepository.save(fl);
//                }
//                fl.addHotel(hotel);
//            }
//            hotelRepository.save(hotel);
//        }
//    }