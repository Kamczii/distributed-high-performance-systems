package pl.rsww.offerread;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.rsww.offerread.locations.getting_locations.LocationRepository;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfoProjection;
import pl.rsww.offerread.offers.getting_offers.OfferShortInfoRepository;
import pl.rsww.offerread.offers.getting_offers.ShortInfoService;

@SpringBootApplication
public class OfferReadApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfferReadApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(OfferShortInfoProjection projection, OfferShortInfoRepository shortInfoRepository, ShortInfoService shortInfoService,
                                 LocationRepository locationRepository) {
        return args -> {
//            OfferIntegrationEvent.Created offerEvent = new OfferIntegrationEvent.Created(
//                    UUID.fromString("b01a4efb-8255-490b-bee6-b0e00c2768df"),  // offerId
//                    new OfferIntegrationEvent.Hotel(
//                            "Ritz Carlton",  // hotel name
//                            new OfferIntegrationEvent.Room("Suite", 2, 2)  // room type, capacity, beds
//                    ),
//                    new OfferIntegrationEvent.Location("New York", "USA"),  // departure location
//                    new OfferIntegrationEvent.Location("Paris", "France"),  // destinationCity location
//                    LocalDate.of(2024, 4, 15),  // start date
//                    LocalDate.of(2024, 4, 25),   // end date,
//                    AvailableOrderStatus.OPEN
//            );
//            projection.listenOffer(offerEvent);
//
//            shortInfoRepository.findAll().forEach(System.out::println);
//
//            shortInfoService.search(new GetOffers());
            shortInfoRepository.deleteAll();
            locationRepository.deleteAll();
        };
    }
}
