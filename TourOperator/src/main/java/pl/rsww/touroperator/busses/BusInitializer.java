package pl.rsww.touroperator.busses;

import pl.rsww.touroperator.busses.lines.BusLine;
import pl.rsww.touroperator.busses.lines.BusLineRepository;
import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.flights.Flight;
import pl.rsww.touroperator.flights.FlightRepository;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItemRepository;
import pl.rsww.touroperator.initialization.PriceListGenerator;
import pl.rsww.touroperator.locations.AirportLocation;
import pl.rsww.touroperator.locations.AirportLocationRepository;

import java.time.LocalDate;
import java.util.*;


public class BusInitializer {
    public final static String HOME_COUNTRY = "Polska";
    public final static int DEFAULT_BUS_NUMBER_OF_PASSENGERS = 20;
    private Map<PlaneConnectionHolder, HashSet<String>> connections;
    private AirportLocationRepository airportLocationRepository;
    private BusRepository busRepository;
    private Map<String, AirportLocation> homeLocations;
    private PriceListGenerator priceListGenerator;
    private Set<AgeRangePriceItem> priceList;
    private AgeRangePriceItemRepository ageRangePriceItemRepository;
    private BusLineRepository busLineRepository;


    private void initPriceList(BusLine busLine){
        priceListGenerator.startFlight();
        List<AgeRangePriceItem> ranges = priceListGenerator.getNextFlightRanges();
        priceList = new HashSet<>(ranges);
        for(AgeRangePriceItem item: priceList){
            item.setBusLine(busLine);
        }
        ageRangePriceItemRepository.saveAll(priceList);
        busLine.setPriceList(priceList);
    }


    public void initBusLines(){
        for(Map.Entry<PlaneConnectionHolder, HashSet<String>> entry: connections.entrySet()){
            String destCity = entry.getKey().getDestinationCity();
            String destCoutry = entry.getKey().getDestinationCountry();
            AirportLocation dest = airportLocationRepository.findByCityAndCountry(destCity, destCoutry);
            for(String city: entry.getValue()){
                AirportLocation departure = homeLocations.get(city);
                if(departure == null){
                    departure = new AirportLocation();
                    departure.setCity(city);
                    departure.setCountry(HOME_COUNTRY);
                    airportLocationRepository.save(departure);
                    homeLocations.put(city, departure);
                }

                BusLine busLine = new BusLine();
                busLine.setDestinationLocation(dest);
                busLine.setHomeLocation(departure);
                busLine.setMaxPassengers(DEFAULT_BUS_NUMBER_OF_PASSENGERS);
                busLineRepository.save(busLine);
                initPriceList(busLine);
                busLineRepository.save(busLine);
            }
        }
    }

    public void initBusses(){
        LocalDate date = LocalDate.now().plusDays(7);;
        LocalDate dateBack = date.plusDays(7);
        Iterable<BusLine> busLines = busLineRepository.findAll();
        for(BusLine busLine: busLines){
            Bus bus = new Bus();
            bus.setLine(busLine);
            bus.setDepartureDate(date);
            bus.setItReturning(false);
            busRepository.save(bus);

            Bus busBack = new Bus();
            busBack.setLine(busLine);
            busBack.setDepartureDate(dateBack);
            busBack.setItReturning(true);
            busRepository.save(busBack);
        }
    }


    public BusInitializer(BusLineRepository busLineRepository, Map<PlaneConnectionHolder, HashSet<String>> connections,
                             AirportLocationRepository airportLocationRepository, BusRepository busRepository,
                             AgeRangePriceItemRepository ageRangePriceItemRepository) {
        this.busLineRepository = busLineRepository;
        this.connections = connections;
        this.airportLocationRepository = airportLocationRepository;
        this.busRepository = busRepository;
        this.ageRangePriceItemRepository = ageRangePriceItemRepository;
        homeLocations = new HashMap<>();
        priceListGenerator = new PriceListGenerator();
    }
}