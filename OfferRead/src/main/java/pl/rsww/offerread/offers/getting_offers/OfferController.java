package pl.rsww.offerread.offers.getting_offers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OfferController {
    private final ShortInfoService shortInfoService;

    @GetMapping("offers")
    public ResponseEntity<List<OfferShortInfoDTO>> search(GetOffers query) {
        log.info(query.toString());
        final var search = shortInfoService.search(query);
        return ResponseEntity.ok(search);
    }

    @GetMapping("/offers/{offerId}")
    public ResponseEntity<OfferShortInfoDTO> getById(@PathVariable UUID offerId,
                                                  @RequestParam(defaultValue = "1") Integer persons,
                                                  @RequestParam Optional<Collection<LocalDate>> kids) {
        final var search = shortInfoService.getById(offerId, persons, kids.orElse(Collections.emptyList()));
        return ResponseEntity.ok(search);
    }
}
