package pl.rsww.touroperator.locations;

import org.springframework.data.repository.CrudRepository;

public interface AirportLocationRepository extends CrudRepository<AirportLocation, Integer> {
    AirportLocation findByCityAndCountry(String cityName, String country);
}
