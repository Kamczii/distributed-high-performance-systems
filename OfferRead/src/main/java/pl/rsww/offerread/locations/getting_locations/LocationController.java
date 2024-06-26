package pl.rsww.offerread.locations.getting_locations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAll() {
        final var search = locationService.getAll();
        return ResponseEntity.ok(search);
    }
}
