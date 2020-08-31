package name.julatec.util.algebraic;

import java.util.Comparator;
import java.util.Optional;

/**
 * Algebraic structure that provides {@code min} and {@code max} operators based on {@code compareTo} operator.
 *
 * @param <T> Type of the lattice.
 */
@FunctionalInterface
public interface Lattice<T> extends Comparator<T> {

    /**
     * Determines the minimum element.
     *
     * @param a first element.
     * @param b second element.
     * @return {@code a} if {@code a} is greater than b, otherwise {@code b}.
     */
    default T max(T a, T b) {
        return compare(a, b) <= 0 ? b : a;
    }

    /**
     * Determines the minimum element.
     *
     * @param a first element.
     * @param b second element.
     * @return {@code a} if {@code b} is greater than a, otherwise {@code b}.
     */
    default T min(T a, T b) {
        return compare(a, b) <= 0 ? a : b;
    }

    /**
     * Determines the minimum element.
     *
     * @param a first element.
     * @param b second element.
     * @return {@code a} if {@code a} is greater than b, otherwise {@code b}.
     */
    default Optional<T> max(Optional<T> a, Optional<T> b) {
        if (!a.isPresent()) return b;
        if (!b.isPresent()) return a;
        return compare(a.get(), b.get()) <= 0 ? b : a;
    }

    /**
     * Determines the minimum element.
     *
     * @param a first element.
     * @param b second element.
     * @return {@code a} if {@code b} is greater than a, otherwise {@code b}.
     */
    default Optional<T> min(Optional<T> a, Optional<T> b) {
        if (!a.isPresent()) return b;
        if (!b.isPresent()) return a;
        return compare(a.get(), b.get()) <= 0 ? a : b;
    }
}
