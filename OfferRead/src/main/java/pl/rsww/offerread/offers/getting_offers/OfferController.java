package pl.rsww.offerread.offers.getting_offers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OfferController {
    private final ShortInfoService shortInfoService;

    @GetMapping("offers")
    public ResponseEntity<Page<OfferShortInfo>> search(@PageableDefault Pageable pageable) {
        final var search = shortInfoService.search(pageable);
        return ResponseEntity.ok(search);
    }

    @GetMapping("/offers/{offerId}")
    public ResponseEntity<OfferShortInfo> getById(@PathVariable UUID offerId) {
        final var search = shortInfoService.getById(offerId);
        return ResponseEntity.ok(search);
    }
}
