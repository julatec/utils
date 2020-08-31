package name.julatec.util.collection;

import java.util.Iterator;
import java.util.Optional;

/**
 * Decorates an iterator to support one-element lookahead while iterating.
 *
 * @param <T>
 */
public final class LookAheadIterator<T> implements Iterator<T> {

    /**
     * Underlying iterator.
     */
    private final Iterator<T> target;

    /**
     * Current element.
     */
    private T head;

    /**
     * Decorates the given target iterator.
     * @param target
     */
    public LookAheadIterator(Iterator<T> target) {
        this.target = target;
    }

    @Override
    public boolean hasNext() {
        if (head != null) {
            return true;
        }
        if (target.hasNext()) {
            head = target.next();
            return true;
        }
        return false;
    }

    /**
     * Returns the next element in iteration without advancing the underlying iterator.
     * @return head of the underlying iterator.
     */
    public Optional<T> peek() {
        if (hasNext()) {
            return Optional.ofNullable(head);
        }
        return Optional.empty();
    }

    @Override
    public T next() {
        T result = head;
        head = null;
        return result;
    }
}
