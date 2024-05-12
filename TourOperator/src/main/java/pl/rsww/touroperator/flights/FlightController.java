package pl.rsww.touroperator.flights;

import pl.rsww.tour_operator.api.FlightRequests;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.initialization.EventSender;

import java.util.HashSet;
import java.util.Set;


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

        Set<FlightRequests.AgeRangePrice> priceListRequests = new HashSet<>();
        for(AgeRangePriceItem item: line.getPriceList()){
            priceListRequests.add(new FlightRequests.AgeRangePrice(item.getStartingRange(), item.getEndingRange(), item.getPrice()));
        }

        FlightRequests.CreateFlight flightRequest;
        if(!flight.getItReturningFlight()){
            flightRequest = new FlightRequests.CreateFlight(flightNumber, line.getMaxPassengers(), lrHome, lrDest, flight.getDepartureDate(), priceListRequests);
        }else{
            flightRequest = new FlightRequests.CreateFlight(flightNumber, line.getMaxPassengers(), lrDest, lrHome, flight.getDepartureDate(), priceListRequests);
        }

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
