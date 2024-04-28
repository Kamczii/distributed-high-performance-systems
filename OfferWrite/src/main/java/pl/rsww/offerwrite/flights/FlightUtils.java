package pl.rsww.offerwrite.flights;

import java.time.LocalDate;

public class FlightUtils {
    public static String flightId(String flightNumber, LocalDate date) {
        return flightNumber + "/" + date.toString();
    }
}
