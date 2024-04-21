package pl.rsww.offerread.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfo;
import pl.rsww.offerread.offers.getting_offers.ShortInfoService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OfferController {
    private final ShortInfoService shortInfoService;

    @GetMapping("offers")
    public ResponseEntity<Page<OfferShortInfo>> search(@PageableDefault Pageable pageable) {
        Page<OfferShortInfo> search = shortInfoService.search(pageable);
        return ResponseEntity.ok(search);
    }

    @GetMapping("/offers/{offerId}")
    public ResponseEntity<OfferShortInfo> getById(@PathVariable UUID offerId) {
        OfferShortInfo search = shortInfoService.getById(offerId);
        return ResponseEntity.ok(search);
    }
}
