package pl.rsww.offerwrite;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.rsww.dominik.api.FlightRequests;
import pl.rsww.dominik.api.HotelRequests;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.hotels.HotelService;
import pl.rsww.offerwrite.offer.OfferService;
import pl.rsww.offerwrite.offer.getting_offers.Offer;
import pl.rsww.offerwrite.offer.getting_offers.OfferRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class OfferWriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfferWriteApplication.class, args);
    }

}
