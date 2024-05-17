package pl.rsww.touroperator.initialization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.data_extraction.HotelInfo;
import pl.rsww.touroperator.data_extraction.JsonDataExtractor;
import pl.rsww.touroperator.flights.FlightInitializer;
import pl.rsww.touroperator.flights.FlightRepository;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.hotels.HotelInitializer;
import pl.rsww.touroperator.hotels.HotelRepository;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItemRepository;
import pl.rsww.touroperator.hotels.rooms.HotelRoomRepository;
import pl.rsww.touroperator.locations.AirportLocationRepository;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class InitService {
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

    @Async
    public void initialize() {
        log.info("Init startup");
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

        log.info("Init finished");
    }
}
