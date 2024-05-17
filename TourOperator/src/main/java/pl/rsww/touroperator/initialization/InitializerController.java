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
    private InitService initService;

    @GetMapping(path="/init")
    public @ResponseBody String initialize() {
        initService.initialize();
        return "Asynchronous initialization started";
    }

}
