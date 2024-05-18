package pl.rsww.touroperator.busses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.rsww.tour_operator.api.BusRequests;
import pl.rsww.tour_operator.api.FlightRequests;
import pl.rsww.touroperator.busses.lines.BusLine;
import pl.rsww.touroperator.busses.lines.BusLineRepository;
import pl.rsww.touroperator.flights.Flight;
import pl.rsww.touroperator.flights.FlightRepository;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.initialization.EventSender;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class BusService {
    @Autowired
    private BusRepository busRepository;
    private EventSender eventSender;

    @Async
    public void sendRequests() {
        log.info("Started sending busses");
        eventSender = EventSender.getEventSender();
        Iterable<Bus> busses = busRepository.findAll();
        for(Bus bus: busses){
            sendRequest(bus);
        }
        log.info("Finished sending busses");
    }

    private void sendRequest(Bus bus){
        BusLine line = bus.getLine();
        BusRequests.LocationRequest lrHome = new BusRequests.LocationRequest(line.getHomeLocation().getCountry(), line.getHomeLocation().getCity());
        BusRequests.LocationRequest lrDest = new BusRequests.LocationRequest(line.getDestinationLocation().getCountry(), line.getDestinationLocation().getCity());
        String busNumber = line.busNumber();
        String key = busNumber + bus.getDepartureDate();

        Set<BusRequests.AgeRangePrice> priceListRequests = new HashSet<>();
        for(AgeRangePriceItem item: line.getPriceList()){
            priceListRequests.add(new BusRequests.AgeRangePrice(item.getStartingRange(), item.getEndingRange(), item.getPrice()));
        }

        BusRequests.CreateBus busRequest;
        if(!bus.getItReturning()){
            busRequest = new BusRequests.CreateBus(busNumber, line.getMaxPassengers(), lrHome, lrDest, bus.getDepartureDate(), priceListRequests);
        }else{
            busRequest = new BusRequests.CreateBus(busNumber, line.getMaxPassengers(), lrDest, lrHome, bus.getDepartureDate(), priceListRequests);
        }

        eventSender.sendBus(busRequest, key);
    }
}