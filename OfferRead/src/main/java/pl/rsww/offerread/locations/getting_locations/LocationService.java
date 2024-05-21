package pl.rsww.offerread.locations.getting_locations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    public List<Location> getAll() {
        return locationRepository.findAll();
    }
}
