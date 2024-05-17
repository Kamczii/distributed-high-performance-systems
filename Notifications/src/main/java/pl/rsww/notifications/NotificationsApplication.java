package pl.rsww.notifications;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import pl.rsww.offerwrite.api.integration.AvailableOfferStatus;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;
import  pl.rsww.notifications.consumer.NotificationService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
public class NotificationsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationsApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(NotificationService notification) {
        return args -> {

            OfferIntegrationEvent.Created offerEvent = new OfferIntegrationEvent.Created(
                    UUID.fromString("b01a4efb-8255-490b-bee6-b0e00c2768df"),  // offerId
                    new OfferIntegrationEvent.Hotel(
                            "Ritz Carlton",  // hotel name
                            new OfferIntegrationEvent.Room("Suite", 2, 2)  // room type, capacity, beds
                    ),
                    new OfferIntegrationEvent.Location("New York", "USA"),  // departure location
                    new OfferIntegrationEvent.Location("Paris", "France"),  // destinationCity location
                    LocalDate.of(2024, 4, 15),  // start date
                    LocalDate.of(2024, 4, 25),   // end date,
                    AvailableOfferStatus.OPEN,
                    Collections.emptyList()
            );
            notification.listen(offerEvent);
        };
    }

//    public static void main(String[] args) {
//
//        SpringApplication.run(NotificationsApplication.class, args);
//    }
}