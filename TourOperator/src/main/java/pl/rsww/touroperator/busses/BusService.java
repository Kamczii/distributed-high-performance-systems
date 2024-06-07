package pl.rsww.touroperator.busses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.rsww.tour_operator.api.BusRequests;
import pl.rsww.touroperator.busses.lines.BusLine;
import pl.rsww.touroperator.price.AgeRangePriceItem;
import pl.rsww.touroperator.events.EventSender;
import pl.rsww.touroperator.price.PriceListGenerator;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class BusService {
    @Autowired
    private BusRepository busRepository;
    private EventSender eventSender;

    @Async
    public void sendRequests(Integer limit) {
        log.info("Started sending busses");
        eventSender = EventSender.getEventSender();
        Iterable<Bus> busses = busRepository.findAll(Pageable.ofSize(limit));
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
        for(AgeRangePriceItem item: PriceListGenerator.getPriceList(line.getPrice().getPrice())){
            priceListRequests.add(new BusRequests.AgeRangePrice(item.getStartingRange(), item.getEndingRange(), item.getPrice()));
        }

        BusRequests.CreateBus busRequest;
        if(!bus.getIsItReturning()){
            busRequest = new BusRequests.CreateBus(busNumber, line.getMaxPassengers(), lrHome, lrDest, bus.getDepartureDate(), priceListRequests);
        }else{
            busRequest = new BusRequests.CreateBus(busNumber, line.getMaxPassengers(), lrDest, lrHome, bus.getDepartureDate(), priceListRequests);
        }

        eventSender.sendBus(busRequest, key);
    }
}