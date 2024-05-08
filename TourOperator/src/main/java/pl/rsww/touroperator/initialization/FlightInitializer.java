package pl.rsww.touroperator.initialization;

import pl.rsww.touroperator.data.PlaneConnectionHolder;
import pl.rsww.touroperator.flights.Flight;
import pl.rsww.touroperator.flights.FlightRepository;
import pl.rsww.touroperator.flights.lines.FlightLine;
import pl.rsww.touroperator.flights.lines.FlightLineRepository;
import pl.rsww.touroperator.locations.AirportLocation;
import pl.rsww.touroperator.locations.AirportLocationRepository;

import java.time.LocalDate;
import java.util.*;

public class FlightInitializer {
    public final static String HOME_COUNTRY = "Polska";
    public final static int DEFAULT_PLANE_NUMBER_OF_PASSENGERS = 50;
    private Map<PlaneConnectionHolder, HashSet<String>> connections;
    private AirportLocationRepository airportLocationRepository;
    private FlightLineRepository flightLineRepository;
    private FlightRepository flightRepository;
    private Map<String, AirportLocation> homeLocations;

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
                             AirportLocationRepository airportLocationRepository, FlightRepository flightRepository) {
        this.flightLineRepository = flightLineRepository;
        this.connections = connections;
        this.airportLocationRepository = airportLocationRepository;
        this.flightRepository = flightRepository;
        homeLocations = new HashMap<>();
    }
}
