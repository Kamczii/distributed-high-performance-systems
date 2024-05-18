package pl.rsww.offerwrite.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.response.AvailableLockStatus;
import pl.rsww.offerwrite.api.response.OfferResponse;
import pl.rsww.offerwrite.offer.OfferService;
import pl.rsww.offerwrite.offer.OfferStatusService;
import pl.rsww.offerwrite.offer.PriceCalculatorService;
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
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final OfferStatusService offerStatusService;

    @AfterReturning(value = "execution(public * pl.rsww.offerwrite.offer.OfferService.reserveOffer(..)) && args(offerId, orderId,ageOfVisitors)", argNames = "offerId,orderId,ageOfVisitors")
    public void afterReserveOffer(UUID offerId, UUID orderId, Collection<Integer> ageOfVisitors) {
        final var price = calculatePrice(offerId, ageOfVisitors);
        final var event = new OfferResponse.Lock(offerId, orderId, price, AvailableLockStatus.SUCCESS);
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
        final var event = new OfferResponse.Lock(offerId, orderId, BigDecimal.ZERO, AvailableLockStatus.FAIL);
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
}
