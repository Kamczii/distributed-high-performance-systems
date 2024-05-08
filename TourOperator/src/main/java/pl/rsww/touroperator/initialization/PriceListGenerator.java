package pl.rsww.touroperator.initialization;

import pl.rsww.touroperator.hotels.age_ranges.AgeRangePriceItem;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PriceListGenerator {
    private static final double max = 5000.0;
    private static final double min = 1000.0;
    private static final int precision = 2;

    private double mainPrice;
    private List<AgeRangePriceItem> ranges;
    private final MathContext mathContext;

    private void generateRandomPrice(){
        Random rand = new Random();
        mainPrice = rand.nextDouble(max - min + 1) + min;
    }

    private void generateRanges(){
        ranges = new ArrayList<>(4);
        ranges.add(new AgeRangePriceItem(0, 2, BigDecimal.valueOf(0.5 * mainPrice).round(mathContext))); // <0,3)
        ranges.add(new AgeRangePriceItem(3, 9, BigDecimal.valueOf(0.8 * mainPrice).round(mathContext))); // <3,10)
        ranges.add(new AgeRangePriceItem(10, 17, BigDecimal.valueOf(0.9 * mainPrice).round(mathContext))); // <10,18)
        ranges.add(new AgeRangePriceItem(19, 999, BigDecimal.valueOf(mainPrice).round(mathContext))); // <18,999>
    }

    public List<AgeRangePriceItem> getRanges(){
        generateRandomPrice();
        generateRanges();
        return ranges;
    }

    public PriceListGenerator() {
        mathContext = new MathContext(precision);
    }
}
