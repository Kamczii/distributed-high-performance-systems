package pl.rsww.offerwrite;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import pl.rsww.offerwrite.api.requests.FlightRequests;
import pl.rsww.offerwrite.api.requests.HotelRequests;
import pl.rsww.offerwrite.flights.FlightService;
import pl.rsww.offerwrite.hotels.HotelService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class OfferWriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfferWriteApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "command.line.runner",
            value = "enabled",
            havingValue = "true",
            matchIfMissing = true)
    public CommandLineRunner run(HotelService hotelService, FlightService flightService) {
        return args -> {
            hotelService.create(new HotelRequests.CreateHotel(UUID.randomUUID(), "Jagódka",
                    new HotelRequests.LocationRequest("Poland", "Gdańsk"),
                    List.of(
                        new HotelRequests.RoomRequest("Single bed", 1, 1),
                        new HotelRequests.RoomRequest("Double bed", 2, 1),
                        new HotelRequests.RoomRequest("Double bed + single bed", 3, 2)
                    )));

            hotelService.create(new HotelRequests.CreateHotel(UUID.randomUUID(), "Borówka",
                    new HotelRequests.LocationRequest("Poland", "Gdańsk"),
                    List.of(
                            new HotelRequests.RoomRequest("Double bed", 2, 1),
                            new HotelRequests.RoomRequest("Double bed + 2 single beds", 4, 3),
                            new HotelRequests.RoomRequest("Single bed", 1, 1)
                    )));

            hotelService.create(new HotelRequests.CreateHotel(UUID.randomUUID(), "Kaszanka",
                    new HotelRequests.LocationRequest("Poland", "Gdańsk"),
                    List.of(
                            new HotelRequests.RoomRequest("Double bed", 2, 1),
                            new HotelRequests.RoomRequest("Double bed + 2 single beds", 4, 3),
                            new HotelRequests.RoomRequest("Single bed", 1, 1)
                    )));

            hotelService.create(new HotelRequests.CreateHotel(UUID.randomUUID(), "Mariot",
                    new HotelRequests.LocationRequest("Poland", "Warsaw"),
                    List.of(
                            new HotelRequests.RoomRequest("Double bed", 2, 1),
                            new HotelRequests.RoomRequest("Double bed + 2 single beds", 4, 3),
                            new HotelRequests.RoomRequest("Single bed", 1, 1)
                    )));

            flightService.create(new FlightRequests.CreateFlight("GW",
                    10,
                    new FlightRequests.LocationRequest("Poland", "Gdańsk"),
                    new FlightRequests.LocationRequest("Poland", "Warsaw"),
                    LocalDate.now()
                    ));

            flightService.create(new FlightRequests.CreateFlight("WG",
                    10,
                    new FlightRequests.LocationRequest("Poland", "Warsaw"),
                    new FlightRequests.LocationRequest("Poland", "Gdańsk"),
                    LocalDate.now()
            ));

            flightService.create(new FlightRequests.CreateFlight("WG",
                    15,
                    new FlightRequests.LocationRequest("Poland", "Warsaw"),
                    new FlightRequests.LocationRequest("Poland", "Gdańsk"),
                    LocalDate.now().plusDays(5)
            ));

            flightService.create(new FlightRequests.CreateFlight("GW",
                    15,
                    new FlightRequests.LocationRequest("Poland", "Gdańsk"),
                    new FlightRequests.LocationRequest("Poland", "Warsaw"),
                    LocalDate.now().plusDays(5)
            ));
        };
    }
}
