package pl.rsww.offerwrite;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.rsww.tour_operator.api.FlightRequests;
import pl.rsww.tour_operator.api.HotelRequests;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.offer.OfferService;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestCommandLineRunner {
    private final FlightService flightService;

    @Bean
//    @ConditionalOnProperty(
//            prefix = "command.line.runner",
//            value = "enabled",
//            havingValue = "true",
//            matchIfMissing = true)
    public CommandLineRunner run(HotelService hotelService, FlightService flightService, OfferService offerService,
                                 OfferRepository offerRepository) {
        return args -> {
            log.info("Init");
            init(hotelService, flightService);
        };
    }

    private final OfferService offerService;
    private final OfferRepository offerRepository;
//    @Scheduled(initialDelay = 7000, fixedRate = 5000)
//    private void reserve() throws InterruptedException {
//        reservation(0);
//    }
//
//    @Scheduled(initialDelay = 5000, fixedRate = 6000)
//    private void reserve2() throws InterruptedException {
//        reservation(1);
//    }
//
//    @Scheduled(initialDelay = 5000, fixedRate = 10000)
//    private void reserve3() throws InterruptedException {
//        reservation(2);
//    }

    private void reservation(Integer id) throws InterruptedException {
        log.info("Trying by " + id);
        try {
            var offers = offerRepository.findAllByOrderById();
            var offer = offers.get((int) (Math.random() * offers.size()));
            var orderId = UUID.randomUUID();
            offerService.reserveOffer(offer.getId(), orderId, List.of(1));
            offerService.confirmOffer(offer.getId(), orderId);
            log.info("Success by id " + id);
        } catch (Exception e) {
            log.info("Failed by id " + id);
            log.error(e.getMessage());
        }
    }


    private static void init(HotelService hotelService, FlightService flightService) {
        hotelService.handle(new HotelRequests.CreateHotel(UUID.randomUUID(), "Jagódka",
                new HotelRequests.LocationRequest("Poland", "Gdańsk"),
                List.of(
                    new HotelRequests.RoomRequest("Single bed", 1, 1, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))),
                    new HotelRequests.RoomRequest("Double bed", 2, 1, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))),
                    new HotelRequests.RoomRequest("Double bed + single bed", 3, 2, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))))
        ));

        hotelService.handle(new HotelRequests.CreateHotel(UUID.randomUUID(), "Borówka",
                new HotelRequests.LocationRequest("Poland", "Gdańsk"),
                List.of(
                        new HotelRequests.RoomRequest("Double bed", 2, 1, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))),
                        new HotelRequests.RoomRequest("Double bed + 2 single beds", 4, 2, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))),
                        new HotelRequests.RoomRequest("Single bed", 1, 1, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))))));

        hotelService.handle(new HotelRequests.CreateHotel(UUID.randomUUID(), "Kaszanka",
                new HotelRequests.LocationRequest("Poland", "Gdańsk"),
                List.of(
                        new HotelRequests.RoomRequest("Double bed", 2, 1, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))),
                        new HotelRequests.RoomRequest("Double bed + 2 single beds", 4, 2, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))),
                        new HotelRequests.RoomRequest("Single bed", 1, 1, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN)))
                )));

        hotelService.handle(new HotelRequests.CreateHotel(UUID.randomUUID(), "Mariot",
                new HotelRequests.LocationRequest("Poland", "Warsaw"),
                List.of(
                        new HotelRequests.RoomRequest("Double bed", 2, 1, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))),
                        new HotelRequests.RoomRequest("Double bed + 2 single beds", 4, 2, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN))),
                        new HotelRequests.RoomRequest("Single bed", 1, 1, List.of(new HotelRequests.AgeRangePrice(0, 100, BigDecimal.TEN)))
                )));

        flightService.handle(new FlightRequests.CreateFlight(UUID.randomUUID().toString(),
                10,
                new FlightRequests.LocationRequest("Poland", "Gdańsk"),
                new FlightRequests.LocationRequest("Poland", "Warsaw"),
                LocalDate.now()
                , List.of(new FlightRequests.AgeRangePrice(0, 100, BigDecimal.TEN))
        ));

        flightService.handle(new FlightRequests.CreateFlight(UUID.randomUUID().toString(),
                10,
                new FlightRequests.LocationRequest("Poland", "Warsaw"),
                new FlightRequests.LocationRequest("Poland", "Gdańsk"),
                LocalDate.now()
                , List.of(new FlightRequests.AgeRangePrice(0, 100, BigDecimal.TEN))
        ));

        flightService.handle(new FlightRequests.CreateFlight(UUID.randomUUID().toString(),
                15,
                new FlightRequests.LocationRequest("Poland", "Warsaw"),
                new FlightRequests.LocationRequest("Poland", "Gdańsk"),
                LocalDate.now().plusDays(5)
                , List.of(new FlightRequests.AgeRangePrice(0, 100, BigDecimal.TEN))
        ));

        flightService.handle(new FlightRequests.CreateFlight(UUID.randomUUID().toString(),
                15,
                new FlightRequests.LocationRequest("Poland", "Gdańsk"),
                new FlightRequests.LocationRequest("Poland", "Warsaw"),
                LocalDate.now().plusDays(5)
                , List.of(new FlightRequests.AgeRangePrice(0, 100, BigDecimal.TEN))
        ));
    }
}
