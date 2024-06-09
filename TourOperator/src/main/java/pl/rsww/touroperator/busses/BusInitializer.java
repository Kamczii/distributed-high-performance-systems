package pl.rsww.touroperator.busses;

import pl.rsww.touroperator.busses.lines.BusLine;
import pl.rsww.touroperator.busses.lines.BusLineRepository;
import pl.rsww.touroperator.data.DefaultValueConstants;
import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.price.*;
import pl.rsww.touroperator.locations.AirportLocation;
import pl.rsww.touroperator.locations.AirportLocationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


public class BusInitializer {
    public final static String HOME_COUNTRY = DefaultValueConstants.HOME_COUNTRY;
    public final static int NUMBER_OF_PASSENGERS = DefaultValueConstants.DEFAULT_BUS_NUMBER_OF_PASSENGERS;
    private Map<PlaneConnectionHolder, HashSet<String>> connections;
    private AirportLocationRepository airportLocationRepository;
    private BusRepository busRepository;
    private Map<String, AirportLocation> homeLocations;
    private Set<AgeRangePriceItem> priceList;
    private BusLineRepository busLineRepository;
    private PriceRepository priceRepository;
    private PriceListGenerator priceListGenerator;

    private void initPriceList(BusLine busLine){
        BigDecimal priceNumber = priceListGenerator.getPrice(this);
        Price price = new Price();
        price.setPrice(priceNumber);
        price.setBusLine(busLine);
        priceRepository.save(price);
        busLine.setPrice(price);
        busLineRepository.save(busLine);
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
                busLine.setMaxPassengers(NUMBER_OF_PASSENGERS);
                busLineRepository.save(busLine);
                initPriceList(busLine);
                busLineRepository.save(busLine);
            }
        }
    }

    public void initBusses(){
        LocalDate date = LocalDate.now().plusDays(7);
        LocalDate dateBack = date.plusDays(7);
        Iterable<BusLine> busLines = busLineRepository.findAll();
        for(BusLine busLine: busLines){
            Bus bus = new Bus();
            bus.setLine(busLine);
            bus.setDepartureDate(date);
            bus.setIsItReturning(false);
            busRepository.save(bus);

            Bus busBack = new Bus();
            busBack.setLine(busLine);
            busBack.setDepartureDate(dateBack);
            busBack.setIsItReturning(true);
            busRepository.save(busBack);
        }
    }


    public BusInitializer(BusLineRepository busLineRepository, Map<PlaneConnectionHolder, HashSet<String>> connections,
                             AirportLocationRepository airportLocationRepository, BusRepository busRepository,
                          PriceRepository priceRepository) {
        this.busLineRepository = busLineRepository;
        this.connections = connections;
        this.airportLocationRepository = airportLocationRepository;
        this.busRepository = busRepository;
        this.priceRepository = priceRepository;
        homeLocations = new HashMap<>();
        priceListGenerator = new PriceListGenerator();
    }
}