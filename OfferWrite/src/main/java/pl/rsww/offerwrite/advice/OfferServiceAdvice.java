package pl.rsww.offerwrite.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;
import pl.rsww.offerwrite.api.response.AvailableLockStatus;
import pl.rsww.offerwrite.api.response.OfferResponse;
import pl.rsww.offerwrite.offer.OfferStatusService;
import pl.rsww.offerwrite.offer.PriceCalculatorService;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;
import pl.rsww.offerwrite.producer.ObjectRequestKafkaProducer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_RESPONSE_TOPIC;
import static pl.rsww.offerwrite.constants.Constants.LOCK_TIME_IN_SECONDS;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OfferServiceAdvice {
    private static final int WAIT = 15;
;

    private final ObjectRequestKafkaProducer objectRequestKafkaProducer;
    private final PriceCalculatorService priceCalculatorService;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final OfferStatusService offerStatusService;
    private final OfferRepository offerRepository;

    @AfterReturning(value = "execution(public * pl.rsww.offerwrite.offer.OfferService.reserveOffer(..)) && args(offerId, orderId,ageOfVisitors)", argNames = "offerId,orderId,ageOfVisitors")
    public void afterReserveOffer(UUID offerId, UUID orderId, Collection<Integer> ageOfVisitors) {

        final var price = calculatePrice(offerId, ageOfVisitors);

        final var offer = fetchOffer(offerId);
        final var hotel = getHotel(offer);
        final var location = getLocation(offer);

        final var event = new OfferResponse.Lock(offerId, orderId, price, AvailableLockStatus.SUCCESS, hotel, location);
        publish(event);

        executorService.schedule(() -> {
            offerStatusService.updateStatus(offerId);
        }, LOCK_TIME_IN_SECONDS + WAIT, TimeUnit.SECONDS);
    }

    @AfterReturning(value = "execution(public * pl.rsww.offerwrite.offer.OfferService.confirmOffer(..)) && args(offerId, orderId)", argNames = "offerId,orderId")
    public void afterConfirmOffer(UUID offerId, UUID orderId) {
        final var event = new OfferResponse.ConfirmLock(offerId, orderId, AvailableLockStatus.SUCCESS);
        publish(event);
    }

    @AfterThrowing(value = "execution(public * pl.rsww.offerwrite.offer.OfferService.reserveOffer(..)) && args(offerId, orderId,ageOfVisitors)", throwing = "ex", argNames = "offerId,orderId,ageOfVisitors,ex")
    public void handleReserveOfferException(UUID offerId, UUID orderId, Collection<Integer> ageOfVisitors, Exception ex) {
        final var offer = fetchOffer(offerId);
        final var hotel = getHotel(offer);
        final var location = getLocation(offer);

        final var event = new OfferResponse.Lock(offerId, orderId, BigDecimal.ZERO, AvailableLockStatus.FAIL, hotel, location);
        publish(event);
    }

    @AfterThrowing(value = "execution(public * pl.rsww.offerwrite.offer.OfferService.confirmOffer(..)) && args(offerId, orderId)", throwing = "ex", argNames = "offerId,orderId,ex")
    public void handleConfirmOfferException(UUID offerId, UUID orderId, Exception ex) {
        final var event = new OfferResponse.ConfirmLock(offerId, orderId, AvailableLockStatus.FAIL);
        publish(event);
    }

    private BigDecimal calculatePrice(UUID offerId, Collection<Integer> ageOfVisitors) {
        return priceCalculatorService.calculate(offerId, ageOfVisitors);
    }

    private void publish(OfferResponse event) {
        objectRequestKafkaProducer.produce(OFFER_RESPONSE_TOPIC, event);
    }

    private Offer fetchOffer(UUID offerId) {
        return offerRepository.findById(offerId)
                  .orElseThrow(() -> new ResourceNotFoundException(Offer.class.toString(), offerId.toString()));
    }

    private static OfferIntegrationEvent.Location getLocation(Offer offer) {
        return new OfferIntegrationEvent.Location(offer.getHotelRoom().getHotel().getLocation().getCity(), offer.getHotelRoom().getHotel().getLocation().getCountry());
    }

    private static OfferIntegrationEvent.Hotel getHotel(Offer offer) {
        return new OfferIntegrationEvent.Hotel(offer.getHotelRoom().getHotel().getName(), new OfferIntegrationEvent.Room(offer.getHotelRoom().getType(), offer.getHotelRoom().getCapacity(), offer.getHotelRoom().getBeds()));
    }

}
