package pl.rsww.touroperator.initialization;

import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;

import java.math.BigDecimal;
import java.math.MathContext;
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
    private static final int PRECISION = 2;

    private double mainPrice;
    private List<AgeRangePriceItem> ranges;
    private final MathContext mathContext;
    private final Random rand;

    private double generateRandomPrice(double min, double max){
        return rand.nextDouble(max - min + 1) + min;
    }

    private void generateRanges(double referencePrice){
        ranges = new ArrayList<>(4);
        ranges.add(new AgeRangePriceItem(0, 2, BigDecimal.valueOf(0.5 * referencePrice).round(mathContext))); // <0,3)
        ranges.add(new AgeRangePriceItem(3, 9, BigDecimal.valueOf(0.8 * referencePrice).round(mathContext))); // <3,10)
        ranges.add(new AgeRangePriceItem(10, 17, BigDecimal.valueOf(0.9 * referencePrice).round(mathContext))); // <10,18)
        ranges.add(new AgeRangePriceItem(18, 999, BigDecimal.valueOf(referencePrice).round(mathContext))); // <18,999>
    }

    public List<AgeRangePriceItem> getNextRoomRanges(){
        generateRanges(mainPrice);
        mainPrice += generateRandomPrice(20, 200);
        return ranges;
    }

    public List<AgeRangePriceItem> getNextFlightRanges(){
        generateRanges(mainPrice);
        return ranges;
    }

    public List<AgeRangePriceItem> getNextBusRanges(){
        generateRanges(mainPrice);
        return ranges;
    }

    public void startHotel(){
        mainPrice = generateRandomPrice(HOTEL_MIN, HOTEL_MAX);
    }

    public void startFlight(){
        mainPrice = generateRandomPrice(FLIGHT_MIN, FLIGHT_MAX);
    }

    public void startBus(){
        mainPrice = generateRandomPrice(BUS_MIN, BUS_MAX);
    }

    public PriceListGenerator() {
        mathContext = new MathContext(PRECISION);
        rand = new Random();
        mainPrice = 0.0;
    }
}
