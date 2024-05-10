package pl.rsww.touroperator.flights;

import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;
import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItemRepository;
import pl.rsww.touroperator.initialization.PriceListGenerator;
import pl.rsww.touroperator.locations.AirportLocation;
import pl.rsww.touroperator.locations.AirportLocationRepository;

import java.time.LocalDate;
import java.util.*;

public class FlightInitializer {
    public final static String HOME_COUNTRY = "Polska";
    public final static int DEFAULT_PLANE_NUMBER_OF_PASSENGERS = 30;
    private Map<PlaneConnectionHolder, HashSet<String>> connections;
    private AirportLocationRepository airportLocationRepository;
    private FlightLineRepository flightLineRepository;
    private FlightRepository flightRepository;
    private Map<String, AirportLocation> homeLocations;
    private PriceListGenerator priceListGenerator;
    private Set<AgeRangePriceItem> priceList;
    private AgeRangePriceItemRepository ageRangePriceItemRepository;


    private void initPriceList(FlightLine line){
        priceListGenerator.startFlight();
        List<AgeRangePriceItem> ranges = priceListGenerator.getNextFlightRanges();
        priceList = new HashSet<>(ranges);
        for(AgeRangePriceItem item: priceList){
            item.setFlightLine(line);
        }
        ageRangePriceItemRepository.saveAll(priceList);
        line.setPriceList(priceList);
    }


    public void initFlightLines(){
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
//                    departure.setHotels(new LinkedList<>());
                    airportLocationRepository.save(departure);
                    homeLocations.put(city, departure);
                }
                FlightLine flightLine = new FlightLine();
                flightLine.setDestinationLocation(dest);
                flightLine.setHomeLocation(departure);
                flightLine.setMaxPassengers(DEFAULT_PLANE_NUMBER_OF_PASSENGERS);
                flightLineRepository.save(flightLine);
                initPriceList(flightLine);
                flightLineRepository.save(flightLine);
            }
        }
    }

    public void initFlights(){
        LocalDate date = LocalDate.now();
        Iterable<FlightLine> flightLines = flightLineRepository.findAll();
        for(FlightLine flightLine: flightLines){
            Flight flight = new Flight();
            flight.setLine(flightLine);
            flight.setDepartureDate(date);
            flightRepository.save(flight);
        }
    }

    public FlightInitializer(FlightLineRepository flightLineRepository, Map<PlaneConnectionHolder, HashSet<String>> connections,
                             AirportLocationRepository airportLocationRepository, FlightRepository flightRepository,
                             AgeRangePriceItemRepository ageRangePriceItemRepository) {
        this.flightLineRepository = flightLineRepository;
        this.connections = connections;
        this.airportLocationRepository = airportLocationRepository;
        this.flightRepository = flightRepository;
        this.ageRangePriceItemRepository = ageRangePriceItemRepository;
        homeLocations = new HashMap<>();
        priceListGenerator = new PriceListGenerator();
    }
}
