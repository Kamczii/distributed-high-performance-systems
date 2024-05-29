package pl.rsww.touroperator.flights;

import pl.rsww.touroperator.data.DefaultValueConstants;
import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.price.*;
import pl.rsww.touroperator.locations.AirportLocation;
import pl.rsww.touroperator.locations.AirportLocationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class FlightInitializer {
    public final static String HOME_COUNTRY = DefaultValueConstants.HOME_COUNTRY;
    public final static int NUMBER_OF_PASSENGERS = DefaultValueConstants.DEFAULT_PLANE_NUMBER_OF_PASSENGERS;
    private Map<PlaneConnectionHolder, HashSet<String>> connections;
    private AirportLocationRepository airportLocationRepository;
    private FlightLineRepository flightLineRepository;
    private FlightRepository flightRepository;
    private Map<String, AirportLocation> homeLocations;
    private PriceListGenerator priceListGenerator;
    private Set<AgeRangePriceItem> priceList;
    private PriceRepository priceRepository;


    private void initPriceList(FlightLine flightLine){
        BigDecimal priceNumber = priceListGenerator.getPrice(this);
        Price price = new Price();
        price.setPrice(priceNumber);
        price.setFlightLine(flightLine);
        priceRepository.save(price);
        flightLine.setPrice(price);
        flightLineRepository.save(flightLine);
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
                    airportLocationRepository.save(departure);
                    homeLocations.put(city, departure);
                }
                FlightLine flightLine = new FlightLine();
                flightLine.setDestinationLocation(dest);
                flightLine.setHomeLocation(departure);
                flightLine.setMaxPassengers(NUMBER_OF_PASSENGERS);
                flightLineRepository.save(flightLine);
                initPriceList(flightLine);
                flightLineRepository.save(flightLine);
            }
        }
    }

    public void initFlights(){
        LocalDate date = LocalDate.now().plusDays(7);;
        LocalDate dateBack = date.plusDays(7);
        Iterable<FlightLine> flightLines = flightLineRepository.findAll();
        for(FlightLine flightLine: flightLines){
            Flight flight = new Flight();
            flight.setLine(flightLine);
            flight.setDepartureDate(date);
            flight.setIsItReturningFlight(false);
            flightRepository.save(flight);

            Flight flightBack = new Flight();
            flightBack.setLine(flightLine);
            flightBack.setDepartureDate(dateBack);
            flightBack.setIsItReturningFlight(true);
            flightRepository.save(flightBack);
        }
    }

    public FlightInitializer(FlightLineRepository flightLineRepository, Map<PlaneConnectionHolder, HashSet<String>> connections,
                             AirportLocationRepository airportLocationRepository, FlightRepository flightRepository,
                             PriceRepository priceRepository) {
        this.flightLineRepository = flightLineRepository;
        this.connections = connections;
        this.airportLocationRepository = airportLocationRepository;
        this.flightRepository = flightRepository;
        this.priceRepository = priceRepository;
        homeLocations = new HashMap<>();
        priceListGenerator = new PriceListGenerator();
    }
}
