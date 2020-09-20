package name.julatec.util.statistics;

import name.julatec.util.collection.Bag;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * The five-number summary is a set of descriptive statistics that provides information about a dataset.
 * It consists of the five most essential sample percentiles:
 * the sample minimum (smallest observation)
 * the lower quartile or first quartile
 * the median (the middle value)
 * the upper quartile or third quartile
 * the sample maximum (largest observation)
 *
 * @param <V> Type parameter of the Sample.
 */
public final class FiveNumberSummary<V> {

    /**
     * Minimum Observation.
     */
    public final V min;

    /**
     * Lower Quartile.
     */
    public final V lowerQuartile;

    /**
     * Median.
     */
    public final V median;

    /**
     * Upper Quartile.
     */
    public final V upperQuartile;

    /**
     * Maximum
     */
    public final V max;

    /**
     * Constructor.
     *
     * @param min           minimum observation
     * @param lowerQuartile lower quartile
     * @param median        median
     * @param upperQuartile upper quartile
     * @param max           maximum observation
     */
    public FiveNumberSummary(
            V min,
            V lowerQuartile,
            V median,
            V upperQuartile,
            V max) {
        this.min = min;
        this.lowerQuartile = lowerQuartile;
        this.median = median;
        this.upperQuartile = upperQuartile;
        this.max = max;
    }

    /**
     * Applies the given function to this summary.
     *
     * @param function Function to apply
     * @param <R>      Type of the new sample
     * @return new Sample after mapping.
     */
    public <R> FiveNumberSummary<R> map(Function<V, R> function) {
        return new FiveNumberSummary<>(
                function.apply(min),
                function.apply(lowerQuartile),
                function.apply(median),
                function.apply(upperQuartile),
                function.apply(max)
        );
    }

    /**
     * Calculates the five-number summary of the given bag using the field operator for scaling.
     *
     * @param bag           sample histogram.
     * @param fieldOperator used to scale the sample type.
     * @param <K>           key type
     * @param <V>           value type.
     * @return five-number summary of the given histogram.
     */
    public static <K extends Comparable<K>, V extends Comparable<V>>
    Optional<FiveNumberSummary<K>> from(
            Bag<K, V> bag,
            BiFunction<Double, V, V> fieldOperator) {
        return from(bag.toNavigableMap(), bag.getOperator(), fieldOperator);

    }

    /**
     * Calculates the five number summary of the given histogram using the field operator for scaling and the
     * sum operator as the addition.
     *
     * @param navigableMap  histogram of the population.
     * @param sumOperator   operator to perform addition.
     * @param fieldOperator operator to perform scaling.
     * @param <K>           type type.
     * @param <V>           value type
     * @return five number summary of the given histogram.
     */
    public static <K extends Comparable<K>, V extends Comparable<V>>
    Optional<FiveNumberSummary<K>> from(
            NavigableMap<K, V> navigableMap,
            BinaryOperator<V> sumOperator,
            BiFunction<Double, V, V> fieldOperator) {
        if (navigableMap.isEmpty()) {
            return Optional.empty();
        }
        final K min = navigableMap.firstKey();
        final K max = navigableMap.lastKey();
        V cumulative = navigableMap.get(min);
        K lastKey = null;
        final TreeMap<V, K> cumulativeMap = new TreeMap<>();
        int count = 0;
        for (Map.Entry<K, V> kvEntry : navigableMap.entrySet()) {
            if (count++ > 0) {
                cumulativeMap.put(cumulative, lastKey);
                cumulative = sumOperator.apply(cumulative, kvEntry.getValue());
            }
            lastKey = kvEntry.getKey();
        }
        cumulativeMap.put(cumulative, lastKey);
        final V sum = cumulative;
        final K lowerQuartile = cumulativeMap.ceilingEntry(fieldOperator.apply(0.25d, sum)).getValue();
        final K median = cumulativeMap.ceilingEntry(fieldOperator.apply(0.50d, sum)).getValue();
        final K upperQuartile = cumulativeMap.ceilingEntry(fieldOperator.apply(0.75d, sum)).getValue();
        return Optional.of(new FiveNumberSummary<>(min, lowerQuartile, median, upperQuartile, max));
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[")
                .append(min)
                .append(", ")
                .append(lowerQuartile)
                .append(", ")
                .append(median)
                .append(", ")
                .append(upperQuartile)
                .append(", ")
                .append(max)
                .append("]")
                .toString();
    }
}
