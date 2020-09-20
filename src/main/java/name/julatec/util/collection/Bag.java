package name.julatec.util.collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Defines a collection that "counts" the number of times a key object appears in the collection. In this case, the count
 * is defined by the {@link V} group.
 * <p>
 * The binary operator {@link Bag#merge(Bag)}) defines a group.
 *
 * @param <K> The Key type of the collection.
 * @param <V> Group type defined by the binary operator.
 */
public class Bag<K extends Comparable<K>, V> implements Iterable<Map.Entry<K, V>> {

    /**
     * Underlying target collection.
     */
    private final TreeMap<K, V> target;

    /**
     * Binary operator that defines the group {@link V}.
     */
    private final BinaryOperator<V> add;

    /**
     * Defines the new bag using the given binary operator.
     *
     * @param add binary operator that defines the group {@link V}.
     */
    public Bag(BinaryOperator<V> add) {
        this.add = add;
        this.target = new TreeMap<>();
    }

    /**
     * Gets the operator that defines this instance.
     *
     * @return instance operator.
     */
    public BinaryOperator<V> getOperator() {
        return add;
    }

    /**
     * Returns an unmodifiable version of this bag.
     *
     * @return navigable version of the current bag.
     */
    public NavigableMap<K, V> toNavigableMap() {
        return java.util.Collections.unmodifiableNavigableMap(target);
    }


    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return target.entrySet().iterator();
    }

    /**
     * Merges the this object with the given bag that.
     *
     * @param that bag collection with the pair to include on this bag.
     * @return this instance.
     */
    public Bag<K, V> merge(Bag<K, V> that) {
        that.forEach(this::add);
        return this;
    }

    /**
     * Adds the given entry to the bag.
     *
     * @param entry to be included.
     * @return this instance.
     */
    public Bag<K, V> add(Map.Entry<K, V> entry) {
        return add(entry.getKey(), entry.getValue());
    }

    /**
     * Adds the given entry to the bag.
     *
     * @param key   key to include
     * @param count value to add
     * @return this instance.
     */
    public synchronized Bag<K, V> add(K key, V count) {
        target.merge(key, count, add);
        return this;
    }

    /**
     * Gets the count associated with the value.
     *
     * @param key key to lookup.
     * @return value associated to key.
     */
    public V get(K key) {
        return target.get(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key
     */
    public V getOrElse(K key, V defaultValue) {
        return target.getOrDefault(key, defaultValue);
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return this.target.size();
    }

    /**
     * Summarizes this instance using a Stream collector
     *
     * @param collector to use to summarize the data.
     * @param <U>       Summary type.
     * @return result of the collect operation.
     */
    public <U> U collect(Collector<Map.Entry<K, V>, ?, U> collector) {
        return target.entrySet().stream().collect(collector);
    }

    /**
     * Creates a Bag Collector using the given binary operator.
     *
     * @param add operator to combine two given values.
     * @param <K> Key type.
     * @param <V> value type.
     * @return a new Bag Collector.
     */
    public static <K extends Comparable<K>, V>
    Collector<Map.Entry<K, V>, ?, Bag<K, V>>
    collect(BinaryOperator<V> add) {
        return Collector.of(
                () -> new Bag<>(add),
                Bag::add,
                Bag::merge);
    }

    /**
     * Transforms the values of this instance using the given function.
     *
     * @param function function to apply to the values
     * @param <R>      target type of the function.
     * @return a map containing the mapped values.
     */
    public <R> NavigableMap<K, R> mapValues(Function<V, R> function) {
        return target
                .entrySet()
                .stream()
                .map(kvEntry -> Map.entry(kvEntry.getKey(), function.apply(kvEntry.getValue())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o, o2) -> o,
                        TreeMap::new));
    }

    @Override
    public String toString() {
        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
        target.forEach((k, v) -> stringBuilder.append(String.valueOf(k), v));
        return stringBuilder.toString();
    }

}
