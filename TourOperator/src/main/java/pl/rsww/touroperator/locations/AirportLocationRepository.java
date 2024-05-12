package pl.rsww.touroperator.locations;

import org.springframework.data.repository.CrudRepository;

public interface AirportLocationRepository extends CrudRepository<AirportLocation, Integer> {
//    TouristicLocation findByCity(java.lang.String cityName);
    AirportLocation findByCityAndCountry(String cityName, String country);
}
