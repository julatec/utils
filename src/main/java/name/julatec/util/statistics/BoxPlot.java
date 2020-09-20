package name.julatec.util.statistics;

import name.julatec.util.collection.Bag;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @param <K>
 * @param <V>
 */
public class BoxPlot<K extends Comparable<K>, V extends Comparable<V>> {

    /**
     * Histogram of the whole population.
     */
    private final Bag<V, Long> histogram = new Bag<>(Long::sum);

    /**
     * Histograms of the population groups.
     */
    private final Bag<K, Bag<V, Long>> histograms = new Bag<>(Bag::merge);

    /**
     * Provides a consumer for peeking values from a stream.
     *
     * @param function function to convert the given type into a Key Value pair.
     * @param <T>      source type
     * @return Consumer values to create histograms.
     */
    public <T> Consumer<T> tee(Function<T, Map.Entry<K, V>> function) {
        return t -> {
            final Map.Entry<K, V> entry = function.apply(t);
            histogram.add(entry.getValue(), 1l);
            histograms.add(entry.getKey(),
                    new Bag<V, Long>(Long::sum)
                            .add(entry.getValue(), 1l));
        };
    }

    /**
     * Simple linear scale function for Long
     *
     * @param scalar factor to scale.
     * @param value  value.
     * @return scaled value.
     */
    private static long scaleInt(Double scalar, long value) {
        return (long) (scalar * value);
    }

    /**
     * Calculate the five number summary of the given histogram.
     *
     * @param histogram population sample
     * @param <V>       sample type.
     * @return five-number summary of the given histogram.
     */
    private static <V extends Comparable<V>>
    Optional<FiveNumberSummary<V>> getSummary(Bag<V, Long> histogram) {
        return FiveNumberSummary.from(histogram, BoxPlot::scaleInt);
    }

    /**
     * Five-number summary of the whole sample.
     *
     * @return five-number summary of the whole sample.
     */
    public Optional<FiveNumberSummary<V>> getSummary() {
        return getSummary(histogram);
    }

    /**
     * Five-number summary of each group.
     *
     * @return five-number summary of each group.
     */
    public NavigableMap<K, Optional<FiveNumberSummary<V>>> getSummaries() {
        return histograms.mapValues(BoxPlot::getSummary);
    }
}