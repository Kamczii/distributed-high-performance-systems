package pl.rsww.offerwrite.advice;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pl.rsww.offerwrite.api.response.AvailableLockStatus;
import pl.rsww.offerwrite.api.response.OfferResponse;
import pl.rsww.offerwrite.offer.PriceCalculatorService;
import pl.rsww.offerwrite.producer.ObjectRequestKafkaProducer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

import static pl.rsww.offerwrite.api.OfferWriteTopics.OFFER_RESPONSE_TOPIC;

@Aspect
@Component
@RequiredArgsConstructor
public class OfferServiceAdvice {
    private final ObjectRequestKafkaProducer objectRequestKafkaProducer;
    private final PriceCalculatorService priceCalculatorService;

    @AfterReturning(value = "execution(public * pl.rsww.offerwrite.offer.OfferService.reserveOffer(..)) && args(offerId, orderId,ageOfVisitors)", argNames = "offerId,orderId,ageOfVisitors")
    public void afterReserveOffer(UUID offerId, UUID orderId, Collection<Integer> ageOfVisitors) {
        final var price = calculatePrice(offerId, ageOfVisitors);
        final var event = new OfferResponse.Lock(offerId, orderId, price, AvailableLockStatus.SUCCESS);
        publish(event);
    }

    @AfterReturning(value = "execution(public * pl.rsww.offerwrite.offer.OfferService.confirmOffer(..)) && args(offerId, orderId)", returning = "result", argNames = "offerId,orderId,result")
    public void afterConfirmOffer(UUID offerId, UUID orderId, Object result) {
        final var event = new OfferResponse.ConfirmLock(offerId, orderId, AvailableLockStatus.SUCCESS);
        publish(event);
    }

    @AfterThrowing(value = "execution(public * pl.rsww.offerwrite.offer.OfferService.reserveOffer(..)) && args(offerId, orderId)", throwing = "ex", argNames = "offerId,orderId,ex")
    public void handleReserveOfferException(UUID offerId, UUID orderId, Exception ex) {
        final var event = new OfferResponse.Lock(offerId, orderId, BigDecimal.TEN, AvailableLockStatus.FAIL);
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
