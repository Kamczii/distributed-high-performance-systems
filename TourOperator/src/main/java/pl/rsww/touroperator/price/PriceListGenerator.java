package pl.rsww.touroperator.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.rsww.touroperator.busses.BusInitializer;
import pl.rsww.touroperator.flights.FlightInitializer;
import pl.rsww.touroperator.hotels.HotelInitializer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PriceListGenerator {
    private static final double HOTEL_MAX = 5000.0;
    private static final double HOTEL_MIN = 1000.0;
    private static final double FLIGHT_MAX = 1000.0;
    private static final double FLIGHT_MIN = 500.0;
    private static final double BUS_MAX = 900.0;
    private static final double BUS_MIN = 200.0;
    private static final double N_ROOM_MIN = 0;
    private static final double N_ROOM_MAX = 200.0;
    private static final Logger log = LoggerFactory.getLogger(PriceListGenerator.class);

    private double mainPrice;
    private final Random rand;

    private double generateRandomPrice(double min, double max){
        return rand.nextDouble(max - min + 1) + min;
    }

    public PriceListGenerator() {
        rand = new Random();
    }

    private double getPriceBus(){
        return generateRandomPrice(BUS_MIN, BUS_MAX);
    }

    private double getPriceFlight(){
        return generateRandomPrice(FLIGHT_MIN, FLIGHT_MAX);
    }

    private double getPriceHotel(){
        return generateRandomPrice(HOTEL_MIN, HOTEL_MAX);
    }

    public BigDecimal getPrice(Object obj){
        if(obj instanceof BusInitializer){
            mainPrice = getPriceBus();
        } else if (obj instanceof FlightInitializer) {
            mainPrice = getPriceFlight();
        } else if (obj instanceof HotelInitializer) {
            mainPrice = getPriceHotel();
        }else{
            log.info("Wrong object class in PriceListGenerator method call");
            mainPrice = 0.0;
        }
        return BigDecimal.valueOf(mainPrice).round(PriceTools.getContext());
    }

    public BigDecimal getNextRoomPrice(){
        mainPrice += generateRandomPrice(N_ROOM_MIN, N_ROOM_MAX);
        return BigDecimal.valueOf(mainPrice).round(PriceTools.getContext());
    }

    public static List<AgeRangePriceItem> getPriceList(BigDecimal referencePrice){
        List<AgeRangePriceItem> ranges = new ArrayList<>(4);
        ranges.add(new AgeRangePriceItem(0, 2, referencePrice, BigDecimal.valueOf(0.5))); // <0,3)
        ranges.add(new AgeRangePriceItem(3, 9, referencePrice, BigDecimal.valueOf(0.8))); // <3,10)
        ranges.add(new AgeRangePriceItem(10, 17, referencePrice, BigDecimal.valueOf(0.9))); // <10,18)
        ranges.add(new AgeRangePriceItem(18, 999, referencePrice, BigDecimal.valueOf(1))); // <18,999>
        return ranges;
    }
}
