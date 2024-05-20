package pl.rsww.touroperator.flights;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import pl.rsww.touroperator.flights.lines.FlightLine;
import org.springframework.data.repository.CrudRepository;


public interface FlightRepository extends CrudRepository<Flight, Integer> {
    Iterable<Flight> findAllByLine(FlightLine line);
    Iterable<Flight> findAll(Pageable pageable);
}
