package pl.rsww.touroperator.flights;

import pl.rsww.dominik.api.FlightRequests;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.hotels.Hotel;
import pl.rsww.touroperator.hotels.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.rsww.touroperator.initialization.EventSender;


@Controller
@RequestMapping(path="/flights")
public class FlightController {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private FlightLineRepository flightLineRepository;
    private EventSender eventSender;


    private void sendRequest(Flight flight){
        FlightLine line = flight.getLine();
        FlightRequests.LocationRequest lrHome = new FlightRequests.LocationRequest(line.getHomeLocation().getCountry(), line.getHomeLocation().getCity());
        FlightRequests.LocationRequest lrDest = new FlightRequests.LocationRequest(line.getDestinationLocation().getCountry(), line.getDestinationLocation().getCity());
        String flightNumber = line.flightNumber();
        String key = flightNumber + flight.getDepartureDate();
        FlightRequests.CreateFlight flightRequest = new FlightRequests.CreateFlight(flightNumber, line.getMaxPassengers(), lrHome, lrDest, flight.getDepartureDate());
        eventSender.sendFlight(flightRequest, key);
    }

    @GetMapping(path="/send")
    public @ResponseBody String sendRequests() {
        eventSender = EventSender.getEventSender();
        Iterable<Flight> flights = flightRepository.findAll();
        for(Flight flight: flights){
            sendRequest(flight);
        }
        return "OK";
    }
}
