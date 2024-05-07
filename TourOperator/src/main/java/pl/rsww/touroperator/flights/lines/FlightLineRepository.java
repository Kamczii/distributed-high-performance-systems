package pl.rsww.touroperator.flights.lines;

import org.springframework.data.repository.CrudRepository;
import pl.rsww.touroperator.locations.AirportLocation;

public interface FlightLineRepository extends CrudRepository<FlightLine, Integer> {
//    FlightLine findByHomeAirportAndDestinationAirport(String homeAirport, String destinationAirport);
//FlightLine findByHomeLocationAndDestinationLocation(AirportLocation homeLocation, AirportLocation destinationLocation);
}
