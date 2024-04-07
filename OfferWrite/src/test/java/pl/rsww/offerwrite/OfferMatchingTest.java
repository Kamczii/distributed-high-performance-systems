package pl.rsww.offerwrite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.rsww.offerwrite.api.requests.FlightRequests;
import pl.rsww.offerwrite.api.requests.HotelRequests;
import pl.rsww.offerwrite.offer.OfferService;

@ExtendWith(MockitoExtension.class)
public class OfferMatchingTest {

    @InjectMocks
    private OfferService offerService;

    @BeforeEach
    void init() {
        FlightRequests.CreateFlight[] flights = FlightTest.test();
        HotelRequests.CreateHotel[] hotels = HotelsTest.test();
    }

    @Test
    public void test() {

    }
}
