package pl.rsww.touroperator.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.data_extraction.HotelInfo;
import pl.rsww.touroperator.data_extraction.JsonDataExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.rsww.touroperator.flights.FlightInitializer;
import pl.rsww.touroperator.flights.FlightRepository;
//import pl.rsww.touroperator.flights.bookings.FlightBookingRepository;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.hotels.HotelInitializer;
import pl.rsww.touroperator.hotels.HotelRepository;
//import pl.rsww.touroperator.hotels.reservations.RoomReservationRepository;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItemRepository;
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
    private FlightLineRepository flightLineRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportLocationRepository airportLocationRepository;
    @Autowired
    private AgeRangePriceItemRepository ageRangePriceItemRepository;

    @GetMapping(path="/init")
    public @ResponseBody String initialize() {

        JsonDataExtractor extractor = new JsonDataExtractor();
        List<HotelInfo> hotelInfoList = extractor.extract();
        HotelInitializer hotelInitializer = new HotelInitializer(hotelRepository, hotelRoomRepository, airportLocationRepository, ageRangePriceItemRepository);

//        EventSender eventSender = new EventSender();
        Map<PlaneConnectionHolder, HashSet<String>> connections = new Hashtable<>();

//        initializer.setEventSender(eventSender);
        hotelInitializer.setPlaneConnections(connections);

        for(HotelInfo info: hotelInfoList){
            hotelInitializer.initialize(info);
            hotelInitializer.clear();
        }

        FlightInitializer flightInitializer = new FlightInitializer(flightLineRepository, connections, airportLocationRepository, flightRepository, ageRangePriceItemRepository);
        flightInitializer.initFlightLines();
        flightInitializer.initFlights();

        return "OK";
    }

}
