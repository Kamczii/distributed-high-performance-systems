package pl.rsww.preference.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.OfferWriteTopics;
import pl.rsww.offerwrite.api.response.AvailableLockStatus;
import pl.rsww.offerwrite.api.response.OfferResponse;
import pl.rsww.preference.service.PreferenceService;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OfferResponseListener {
    private final PreferenceService preferenceService;

    @KafkaListener(topics = OfferWriteTopics.OFFER_RESPONSE_TOPIC, containerFactory = "lockFactory")
    public void listenOffer(OfferResponse offerResponse) {
        if (Objects.requireNonNull(offerResponse) instanceof OfferResponse.Lock lock) {
            log.info(String.format("Received offer reserved event: %s", offerResponse));
            if (lock.status() == AvailableLockStatus.SUCCESS) {
                preferenceService.addDestination(lock.location().country(), lock.location().city());
                preferenceService.addHotelAndRoom(lock.hotel().name(), lock.hotel().room().type(), lock.hotel().room().capacity(), lock.hotel().room().beds());
            }
        } else {
            log.info(String.format("Ignored offer response event: %s", offerResponse));
        }
    }
}
