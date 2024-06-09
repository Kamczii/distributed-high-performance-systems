package pl.rsww.touroperator.initialization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.rsww.touroperator.busses.BusInitializer;
import pl.rsww.touroperator.busses.BusRepository;
import pl.rsww.touroperator.busses.lines.BusLineRepository;
import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.data.HotelInfo;
import pl.rsww.touroperator.data_extraction.JsonDataExtractor;
import pl.rsww.touroperator.flights.FlightInitializer;
import pl.rsww.touroperator.flights.FlightRepository;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.hotels.HotelInitializer;
import pl.rsww.touroperator.hotels.HotelRepository;
import pl.rsww.touroperator.hotels.rooms.HotelRoomRepository;
import pl.rsww.touroperator.locations.AirportLocationRepository;
import pl.rsww.touroperator.price.PriceRepository;

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
    private BusRepository busRepository;
    @Autowired
    private BusLineRepository busLineRepository;
    @Autowired
    private AirportLocationRepository airportLocationRepository;
    @Autowired
    private PriceRepository priceRepository;

    @Async
    public void initialize() {
        log.info("Init startup");
        JsonDataExtractor extractor = new JsonDataExtractor();
        List<HotelInfo> hotelInfoList = extractor.extract();
        HotelInitializer hotelInitializer = new HotelInitializer(hotelRepository, hotelRoomRepository, airportLocationRepository, priceRepository);

        Map<PlaneConnectionHolder, HashSet<String>> connections = new Hashtable<>();

        hotelInitializer.setPlaneConnections(connections);

        for(HotelInfo info: hotelInfoList){
            hotelInitializer.initialize(info);
            hotelInitializer.clear();
        }

        FlightInitializer flightInitializer = new FlightInitializer(flightLineRepository, connections, airportLocationRepository, flightRepository, priceRepository);
        flightInitializer.initFlightLines();
        flightInitializer.initFlights();

        BusInitializer busInitializer = new BusInitializer(busLineRepository, connections, airportLocationRepository, busRepository, priceRepository);
        busInitializer.initBusLines();
        busInitializer.initBusses();

        log.info("Init finished");
    }
}
