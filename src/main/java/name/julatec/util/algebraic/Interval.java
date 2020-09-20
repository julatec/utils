package name.julatec.util.algebraic;

import java.util.function.Function;

/**
 * This class represents a 1D interval for the Sorted Set T.
 *
 * @param <T>
 */
public class Interval<T extends Comparable<T>> {

    /**
     * Lower bound.
     */
    public final T lower;

    /**
     * Whether includes the lower bound.
     */
    public final boolean lowerOpen;

    /**
     * Upper bound.
     */
    public final T upper;

    /**
     * Whether includes the upper bound.
     */
    public final boolean upperOpen;

    /**
     * Defines a closed interval with the given lower and upper bound.
     *
     * @param lower lower bound.
     * @param upper upper bound.
     * @param <T>   interval sorted type.
     * @return new Interval instance.
     */
    public static <T extends Comparable<T>> Interval<T> of(T lower, T upper) {
        return new Interval<>(lower, false, upper, false);
    }

    /**
     * Defines a interval with the given lower and upper bound and the indications whether the bounds are open.
     *
     * @param lower     lower bound.
     * @param lowerOpen whether lower bound is open.
     * @param upper     upper bound.
     * @param upperOpen whether upper bound is open.
     */
    public Interval(T lower, boolean lowerOpen, T upper, boolean upperOpen) {
        this.lower = lower;
        this.lowerOpen = lowerOpen;
        this.upper = upper;
        this.upperOpen = upperOpen;
    }

    /**
     * Defines a interval with the given lower and upper bound and the indications whether the bounds are open.
     *
     * @param lower     lower bound.
     * @param lowerOpen whether lower bound is open.
     * @param upper     upper bound.
     * @param upperOpen whether upper bound is open.
     * @return new Interval instance.
     */
    public static <T extends Comparable<T>> Interval<T> of(T lower, boolean lowerOpen, T upper, boolean upperOpen) {
        return new Interval(lower, lowerOpen, upper, upperOpen);
    }

    /**
     * Checks if a value is contained in the bounds.
     *
     * @param value value to test
     * @return true if value is inside the bounds, otherwise false.
     */
    public boolean contains(T value) {
        final int lowerTest = lower.compareTo(value);
        final int upperTest = upper.compareTo(value);
        if (lowerTest > 0) {
            return false;
        }
        if (upperTest < 0) {
            return false;
        }
        if (!lowerOpen && lowerTest == 0) {
            return true;
        }
        if (!upperOpen && upperTest == 0) {
            return true;
        }
        return lowerTest < 0 && upperTest > 0;
    }

    /**
     * Provides a projection function that scales using linear Scale from the source interval to the target interval.
     *
     * @param source  source interval to scale.
     * @param target  target interval to scale.
     * @param mapping maps from reals to target interval.
     * @param <N>     Type of the source interval.
     * @param <M>     Type of the target interval.
     * @return linear scalar function from source interval to target interval.
     */
    public static <N extends Number & Comparable<N>, M extends Number & Comparable<M>> Function<N, M> scale(
            Interval<N> source,
            Interval<M> target,
            Function<Double, M> mapping) {
        final double sourceRange = source.upper.doubleValue() - source.lower.floatValue();
        final double targetRange = target.upper.doubleValue() - target.lower.floatValue();
        return n -> {
            double doubleValue = target.lower.doubleValue() +
                    targetRange * ((n.doubleValue() - source.lower.doubleValue()) / sourceRange);
            return mapping.apply(doubleValue);
        };
    }

    @Override
    public String toString() {
        return (lowerOpen ? "]" : "[") + lower + ", " + upper + (upperOpen ? "[" : "]");
    }
}
