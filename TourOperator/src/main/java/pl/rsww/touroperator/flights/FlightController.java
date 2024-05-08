package pl.rsww.touroperator.flights;

import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.hotels.Hotel;
import pl.rsww.touroperator.hotels.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/flights")
public class FlightController {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<FlightLine> getAllFlightsToHotel(@RequestParam String hotelName) {
        Hotel hotel = hotelRepository.findByName(hotelName);
//        Set<FlightLine> flightLineSet = h.getFlightLines();
//        List<Flight> flights = new LinkedList<>();
//        for(FlightLine fl: flightLineSet){
//            List<Flight> = flightRepository.findAllByFlightLine(fl);
//        }
        //Iterable<Fli>
        return hotel.getFlightLines();
    }

    @GetMapping(path="/free-in")
    public @ResponseBody Iterable<Hotel> getFreeSitsNumberInFlight(@RequestParam String hotelName) {
        Hotel h = hotelRepository.findByName(hotelName);
        //Iterable<Fli>
        return hotelRepository.findAll();
    }
}
