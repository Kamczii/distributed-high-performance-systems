package pl.rsww.offerwrite.common.age_range_price;

import org.springframework.data.util.Pair;
import pl.rsww.offerwrite.offer.PriceCalculatorService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AgeRangePriceHelper {

    public static Collection<AgeRangePrice> calculateSummedPrices(List<Collection<AgeRangePrice>> collections) {
        final var maxAge = collections.stream().flatMap(Collection::stream).map(AgeRangePrice::endingRange).max(Comparator.comparing(val -> val))
                .orElse(0);
        List<Pair<Integer, BigDecimal>> prices = IntStream.rangeClosed(0, maxAge)
                .mapToObj(age -> Pair.of(
                        age,
                        collections.stream()
                                .map(ranges -> ranges.stream().filter(range -> age >= range.startingRange() && age <= range.endingRange()).findAny().map(AgeRangePrice::price).orElse(BigDecimal.ZERO))
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                )).toList();

        return prices.stream()
                .collect(Collectors.groupingBy(Pair::getSecond,
                        Collectors.mapping(Pair::getFirst, Collectors.collectingAndThen(
                                Collectors.toList(),
                                AgeRangePriceHelper::groupConsecutive
                        )))).entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(pair -> new AgeRangePrice(pair.getFirst(), pair.getSecond(), entry.getKey())))
                .sorted(Comparator.comparing(AgeRangePrice::price))
                .toList();
    }

    private static List<Pair<Integer, Integer>> groupConsecutive(List<Integer> list) {
        list = list.stream().sorted().toList();
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> currentGroup = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            int current = list.get(i);

            if (currentGroup.isEmpty() || currentGroup.get(currentGroup.size() - 1) + 1 == current) {
                currentGroup.add(current);
            } else {
                result.add(new ArrayList<>(currentGroup));
                currentGroup.clear();
                currentGroup.add(current);
            }
        }

        if (!currentGroup.isEmpty()) {
            result.add(currentGroup);
        }

        return result.stream()
                .map(l -> {
                    var st = l.stream().mapToInt(value -> value).summaryStatistics();
                    return Pair.of(st.getMin(), st.getMax());
                })
                .sorted(Comparator.comparing(Pair::getFirst))
                .toList();
    }
}
