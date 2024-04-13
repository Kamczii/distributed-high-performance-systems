package pl.rsww.order.event.offer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class OfferBookedEvent {
    private UUID offerId;
}
