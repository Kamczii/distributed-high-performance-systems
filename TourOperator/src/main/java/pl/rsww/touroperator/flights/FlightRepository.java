package pl.rsww.touroperator.flights;

import pl.rsww.touroperator.flights.lines.FlightLine;
import org.springframework.data.repository.CrudRepository;


public interface FlightRepository extends CrudRepository<Flight, Integer> {
    Iterable<Flight> findAllByLine(FlightLine line);
}
