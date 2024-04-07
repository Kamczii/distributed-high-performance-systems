package pl.rsww.offerwrite;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.rsww.offerwrite.api.requests.HotelRequests;
import pl.rsww.offerwrite.hotels.HotelService;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class OfferWriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfferWriteApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(HotelService hotelService) {
        return args -> {
            hotelService.create(new HotelRequests.CreateHotel(UUID.randomUUID(), new HotelRequests.LocationRequest("Poland", "Gdańsk"), List.of(
                    new HotelRequests.RoomRequest("Single bed", 1)
            )));
        };
    }
}
