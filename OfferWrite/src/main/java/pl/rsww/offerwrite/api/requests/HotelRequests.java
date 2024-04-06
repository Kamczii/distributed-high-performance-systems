package pl.rsww.offerwrite.api.requests;

import java.util.UUID;

public final class HotelRequests {

    public record Create(
            UUID hotelId
    ) {}
}
