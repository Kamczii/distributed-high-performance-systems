package pl.rsww.offerread.locations.getting_locations;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface LocationRepository extends MongoRepository<Location, UUID> {
}
