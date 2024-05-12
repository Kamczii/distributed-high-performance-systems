package pl.rsww.offerwrite;

import pl.rsww.offerwrite.common.age_range_price.AgeRangePrice;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AgeRangePriceHelperTest {

    @Test
    public void testCalculateSummedPrices() {
        // Setup test data
        AgeRangePrice returnFlight1 = new AgeRangePrice(0, 10, new BigDecimal("10"));
        AgeRangePrice returnFlight2 = new AgeRangePrice(11, 1000, new BigDecimal("15"));

        AgeRangePrice initialFlight1 = new AgeRangePrice(0, 5, new BigDecimal("5"));
        AgeRangePrice initialFlight2 = new AgeRangePrice(6, 1000, new BigDecimal("10"));

        AgeRangePrice hotel1 = new AgeRangePrice(0, 12, new BigDecimal("20"));
        AgeRangePrice hotel2 = new AgeRangePrice(13, 1000, new BigDecimal("25"));


        Collection<AgeRangePrice> returnFlights = Arrays.asList(returnFlight1, returnFlight2);
        Collection<AgeRangePrice> initialFlights = Arrays.asList(initialFlight1, initialFlight2);
        Collection<AgeRangePrice> hotels = Arrays.asList(hotel1, hotel2);

        List<Collection<AgeRangePrice>> collections = Arrays.asList(returnFlights, initialFlights, hotels);

        // Create an instance of PriceCalculator
        List<AgeRangePrice> expectedResults = Arrays.asList(
                new AgeRangePrice(0, 5, new BigDecimal("35")),   // Sum of 10 + 5 + 20
                new AgeRangePrice(6, 10, new BigDecimal("40")),  // Sum of 10 + 10 + 10 (overlap with returnFlight1 and initialFlight2)
                new AgeRangePrice(11, 12, new BigDecimal("45")), // Sum of 15 + 20 (overlap between returnFlight2 and hotel1)
                new AgeRangePrice(13, 1000, new BigDecimal("50")) // Sum of 15 + 10 + 10 (overlap with returnFlight2 and initialFlight2)
        );

        // Execute the method under test
        Collection<AgeRangePrice> results = pl.rsww.offerwrite.common.age_range_price.AgeRangePriceHelper.calculateSummedPrices(collections);

        // Define expected results


        // Check the expected size
        assertEquals(expectedResults.size(), results.size(), "Result size should match expected size.");

        // Check the results content
        AgeRangePrice[] resultArray = results.toArray(new AgeRangePrice[0]);
        for (int i = 0; i < resultArray.length; i++) {
            var actual = resultArray[i];
            var expected = expectedResults.get(i);
            assertEquals(expected.startingRange(), actual.startingRange(), "Starting range should match.");
            assertEquals(expected.endingRange(), actual.endingRange(), "Ending range should match.");
            assertEquals(expected.price(), actual.price(), "Prices should be equal.");
        }
    }

}
