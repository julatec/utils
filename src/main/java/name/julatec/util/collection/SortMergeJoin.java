package name.julatec.util.collection;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterators.spliteratorUnknownSize;

/**
 * The sort-merge join (also known as merge join) is a join algorithm and is used in the implementation of a relational
 * database management system.
 *
 * @param <L> Left part of the join.
 * @param <R> Right part of the join.
 * @param <M> Result of the join of L and R.
 */
public interface SortMergeJoin<L, R, M> {

    /**
     * Compares the given left instance to the right instance and determines the order relation.
     * @param left
     * @param right
     * @return
     */
    int compare(L left, R right);

    /**
     * Merges the given {@code merged} value with the given {@code right value}.
     * @param merged current value.
     * @param right right value.
     * @return merged result of {@code merged} and {@code right}.
     */
    M rightMerge(Optional<M> merged, R right);

    /**
     * Merges the given {@code merged} value with the given {@code left value}.
     * @param merged current value.
     * @param left left value.
     * @return merged result of {@code merged} and {@code r}.
     */
    M leftMerge(Optional<M> merged, L left);

    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    int leftCompare(L a, L b);

    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    int rightCompare(R a, R b);

    /**
     * Merges two given streams.
     * @param left the first stream to merge.
     * @param right the second stream to merge.
     * @return merged stream.
     */
    default Stream<M> merge(Stream<L> left, Stream<R> right) {
        final LookAheadIterator<L> lLookAheadIterator = new LookAheadIterator<>(left.sorted(this::leftCompare).iterator());
        final LookAheadIterator<R> rLookAheadIterator = new LookAheadIterator<>(right.sorted(this::rightCompare).iterator());
        Iterator<M> iterator = new Iterator<>() {

            Optional<M> current = Optional.empty();

            @Override
            public boolean hasNext() {
                if (current.isPresent()) {
                    return true;
                }
                switch ((lLookAheadIterator.hasNext() ? 0b10 : 0) | (rLookAheadIterator.hasNext() ? 0b01 : 0)) {
                    case 0b01:
                        current = Optional.of(rightMerge(current, rLookAheadIterator.next()));
                        return true;
                    case 0b10:
                        current = Optional.of(leftMerge(current, lLookAheadIterator.next()));
                        return true;
                    case 0b11:
                        final L l = lLookAheadIterator.peek().get();
                        final R r = rLookAheadIterator.peek().get();
                        final int comparison = compare(l, r);
                        if (comparison == 0) {
                            current = Optional.empty();
                        }
                        if (comparison >= 0) {
                            current = Optional.of(rightMerge(current, rLookAheadIterator.next()));
                        }
                        if (comparison <= 0) {
                            current = Optional.of(leftMerge(current, lLookAheadIterator.next()));
                        }

                        return true;
                    default:
                        return false;

                }
            }

            @Override
            public M next() {
                M result = current.get();
                current = Optional.empty();
                return result;
            }
        };
        final Spliterator<M> spliterator = spliteratorUnknownSize(iterator, Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false);
    }

    static <L, R, M> SortMergeJoin<L, R, M> of(
            BiFunction<L, R, Integer> compare,
            BiFunction<Optional<M>, L, M> lMerge,
            BiFunction<Optional<M>, R, M> rMerge,
            Comparator<L> lComparator,
            Comparator<R> rComparator) {
        return new SortMergeJoinImpl<>(compare, lMerge, rMerge, lComparator, rComparator);
    }
}

final class SortMergeJoinImpl<L, R, M> implements SortMergeJoin<L, R, M> {

    private final BiFunction<L, R, Integer> compare;

    private final BiFunction<Optional<M>, L, M> lMerge;

    private final BiFunction<Optional<M>, R, M> rMerge;

    private final Comparator<L> lComparator;

    private final Comparator<R> rComparator;

    SortMergeJoinImpl(
            BiFunction<L, R, Integer> compare,
            BiFunction<Optional<M>, L, M> lMerge,
            BiFunction<Optional<M>, R, M> rMerge,
            Comparator<L> lComparator,
            Comparator<R> rComparator) {
        this.compare = compare;
        this.lMerge = lMerge;
        this.rMerge = rMerge;
        this.lComparator = lComparator;
        this.rComparator = rComparator;
    }

    @Override
    public int compare(L left, R right) {
        return compare.apply(left, right);
    }

    @Override
    public M rightMerge(Optional<M> merged, R right) {
        return rMerge.apply(merged, right);
    }

    @Override
    public M leftMerge(Optional<M> merged, L left) {
        return lMerge.apply(merged, left);
    }

    @Override
    public int leftCompare(L a, L b) {
        return lComparator.compare(a, b);
    }

    @Override
    public int rightCompare(R a, R b) {
        return rComparator.compare(a, b);
    }

}
