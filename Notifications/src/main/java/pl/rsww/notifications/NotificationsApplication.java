package pl.rsww.notifications;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.rsww.notifications.consumer.EventMessage;
import pl.rsww.notifications.consumer.NotificationService;
import pl.rsww.offerwrite.api.integration.AvailableOfferStatus;
import pl.rsww.offerwrite.api.integration.OfferIntegrationEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class NotificationsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationsApplication.class, args);
    }
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void schedule() {

        executorService.schedule(() -> {
            try {
                Random random = new Random();
                AvailableOfferStatus[] statuses = AvailableOfferStatus.values();
                int index = random.nextInt(statuses.length);  // Losuje indeks od 0 do (długość tablicy - 1)
                AvailableOfferStatus randomStatus = statuses[index];  // Pobiera losowy status
                System.out.println("Randomly selected status: " + randomStatus);
                OfferIntegrationEvent.StatusChanged offer_event = new OfferIntegrationEvent.StatusChanged(
                        UUID.fromString("b01a4efb-8255-490b-bee6-b0e00c2768df"),
                        randomStatus
                );
                String timestamp = LocalDateTime.now().format(formatter);
                EventMessage message = new EventMessage("Available Offer Status Changed", offer_event.offerId().toString(), timestamp);
                log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                messagingTemplate.convertAndSend("/topic/notifications", message);
                messagingTemplate.convertAndSend("/topic/notifications/" +  offer_event.offerId().toString(), message);
                log.info("Sent offer change to topic \"/topic/notifications\"");
            } catch (Exception e) {
                log.error("Error sending WebSocket message", e);
            }
        }, 30, TimeUnit.SECONDS);
    }

//    public static void main(String[] args) {
//
//        SpringApplication.run(NotificationsApplication.class, args);
//    }
}