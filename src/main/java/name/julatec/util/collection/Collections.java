package name.julatec.util.collection;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

public final class Collections {

    private Collections() {

    }

    public static <K extends Comparable<K>, V extends Comparable<V>> SortedMap<K, SortedSet<V>> groupBy(
            Function<? super V, K> key,
            Stream<V> values) {
        return values.collect(groupingBy(key, TreeMap::new, toCollection(TreeSet::new)));
    }

    public static <K extends Comparable<K>, V, C extends Collection<V>> SortedMap<K, C> groupBy(
            Function<? super V, K> key,
            Supplier<C> supplier,
            Stream<V> values) {
        return values.collect(groupingBy(key, TreeMap::new, toCollection(supplier)));
    }

    public static <T> SortedSet<T> union(SortedSet<T> records, SortedSet<T> records1) {
        TreeSet<T> ts = new TreeSet<>(records);
        ts.addAll(records1);
        return ts;
    }
}
