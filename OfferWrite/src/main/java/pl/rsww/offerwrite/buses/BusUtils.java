package pl.rsww.offerwrite.buses;

import java.time.LocalDate;

public class BusUtils {
    public static String busId(String busNumber, LocalDate date) {
        return busNumber + "/" + date.toString();
    }
}
