package pl.rsww.offerread.offers.getting_offers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ShortInfoService {

    private final OfferShortInfoRepository shortInfoRepository;

    public Page<OfferShortInfo> search(Pageable pageable) {
        return shortInfoRepository.findAll(pageable);
    }
    public OfferShortInfo getById(UUID offerId) {
        return shortInfoRepository.findById(offerId)
                .orElseThrow();
    }
}
