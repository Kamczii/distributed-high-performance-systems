package pl.rsww.offerread.offers.getting_offers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OfferController {
    private final ShortInfoService shortInfoService;

    @GetMapping("offers")
    public ResponseEntity<List<OfferShortInfo>> search(GetOffers query) {
        log.info(query.toString());
        final var search = shortInfoService.search(query);
        return ResponseEntity.ok(search);
    }

    @GetMapping("/offers/{offerId}")
    public ResponseEntity<OfferShortInfo> getById(@PathVariable UUID offerId) {
        final var search = shortInfoService.getById(offerId);
        return ResponseEntity.ok(search);
    }
}
