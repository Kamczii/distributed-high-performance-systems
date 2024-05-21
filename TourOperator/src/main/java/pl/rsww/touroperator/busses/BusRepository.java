package pl.rsww.touroperator.busses;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BusRepository extends CrudRepository<Bus, Integer> {
    List<Bus> findAll(Pageable pageable);
}
