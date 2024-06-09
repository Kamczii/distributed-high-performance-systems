package pl.rsww.touroperator.hotels;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface HotelRepository extends CrudRepository<Hotel, UUID> {
}
