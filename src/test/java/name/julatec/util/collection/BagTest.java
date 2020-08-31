package name.julatec.util.collection;

import org.junit.jupiter.api.*;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static java.math.BigInteger.valueOf;
import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    private static <K extends Comparable<K>, V> void assertBagEquals(Bag<K, V> actual, Map<K, V> expected) {
        final TreeMap<K, V> treeMap = new TreeMap<>(expected);
        final Iterator<Map.Entry<K, V>> expectedIterator  = treeMap.entrySet().iterator();
        final Iterator<Map.Entry<K, V>> actualIterator  = actual.iterator();
        while (true) {
            assertTrue(expectedIterator.hasNext() == actualIterator.hasNext());
            if (expectedIterator.hasNext()) {
                assertTrue(actualIterator.hasNext());
                assertEquals(expectedIterator.next(), actualIterator.next());
            } else {
                break;
            }
        }
    }

    @Test
    void iterator() {
        final Bag<String, BigInteger> bag = new Bag<String, BigInteger>(BigInteger::add)
                .add("a", new BigInteger("2"))
                .add("c", new BigInteger("3"))
                .add("b", new BigInteger("5"))
                .add("c", new BigInteger("6"));
        assertBagEquals(bag, Map.of(
                "a", valueOf(2),
                "b", valueOf(5),
                "c", valueOf(9)));
    }

    @Test
    void merge() {
        final Bag<String, BigInteger> bag1 = new Bag<String, BigInteger>(BigInteger::multiply)
                .add("d", new BigInteger("2"))
                .add("c", new BigInteger("3"))
                .add("b", new BigInteger("5"))
                .add("a", new BigInteger("6"));
        final Bag<String, BigInteger> bag2 = new Bag<String, BigInteger>(BigInteger::multiply)
                .add("a", new BigInteger("2"))
                .add("c", new BigInteger("3"))
                .add("d", new BigInteger("5"))
                .add("c", new BigInteger("6"));
        assertBagEquals(bag1.merge(bag2), Map.of(
                "a", valueOf(12),
                "b", valueOf(5),
                "c", valueOf(54),
                "d", valueOf(10)));
    }

    @Test
    void add() {
        final Bag<String, BigInteger> bag = new Bag<String, BigInteger>(BigInteger::max)
                .add("a", new BigInteger("2"))
                .add("c", new BigInteger("3"))
                .add("b", new BigInteger("5"))
                .add("c", new BigInteger("6"));
        assertBagEquals(bag, Map.of(
                "a", valueOf(2),
                "b", valueOf(5),
                "c", valueOf(6)));
    }

    @Test
    void testAdd() {
        final Bag<String, BigInteger> bag = new Bag<String, BigInteger>(BigInteger::min)
                .add("a", new BigInteger("2"))
                .add("c", new BigInteger("3"))
                .add("b", new BigInteger("5"))
                .add("c", new BigInteger("6"));
        assertBagEquals(bag, Map.of(
                "a", valueOf(2),
                "b", valueOf(5),
                "c", valueOf(3)));
    }

    @Test
    void get() {
        final Bag<String, BigInteger> bag = new Bag<String, BigInteger>(BigInteger::add)
                .add("a", new BigInteger("2"))
                .add("c", new BigInteger("3"))
                .add("b", new BigInteger("5"))
                .add("c", new BigInteger("6"));
        assertEquals(valueOf(9), bag.get("c"));
    }

    @Test
    void getOrElse() {
        final Bag<String, BigInteger> bag = new Bag<String, BigInteger>(BigInteger::add)
                .add("a", new BigInteger("2"))
                .add("c", new BigInteger("3"))
                .add("b", new BigInteger("5"))
                .add("c", new BigInteger("6"));
        assertEquals(valueOf(9), bag.get("c"));
        assertEquals(valueOf(7), bag.getOrElse("e", valueOf(7)));
    }

    @Test
    void size() {
        final Bag<String, BigInteger> bag = new Bag<String, BigInteger>(BigInteger::min)
                .add("a", new BigInteger("2"))
                .add("c", new BigInteger("3"))
                .add("b", new BigInteger("5"))
                .add("c", new BigInteger("6"));
        assertEquals(3, bag.size());
    }
}