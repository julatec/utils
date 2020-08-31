package name.julatec.util.collection;

import name.julatec.util.algebraic.Interval;
import org.junit.jupiter.api.Test;

import static java.math.BigInteger.valueOf;
import static org.junit.jupiter.api.Assertions.*;

class IntervalTest {

    @Test
    void contains() {
        // Basic Cases
        assertFalse(new Interval<>(valueOf(5), true, valueOf(7), true).contains(valueOf(4)));
        assertTrue(new Interval<>(valueOf(5), true, valueOf(7), true).contains(valueOf(6)));
        assertFalse(new Interval<>(valueOf(5), true, valueOf(7), true).contains(valueOf(8)));

        // Corner Cases for Lower Bound
        assertFalse(new Interval<>(valueOf(5), true, valueOf(7), true).contains(valueOf(5)));
        assertTrue(new Interval<>(valueOf(5), false, valueOf(7), true).contains(valueOf(5)));
        assertFalse(new Interval<>(valueOf(5), true, valueOf(7), false).contains(valueOf(5)));
        assertTrue(new Interval<>(valueOf(5), false, valueOf(7), false).contains(valueOf(5)));

        // Corner Cases for Upper Bound
        assertFalse(new Interval<>(valueOf(5), true, valueOf(7), true).contains(valueOf(7)));
        assertTrue(new Interval<>(valueOf(5), true, valueOf(7), false).contains(valueOf(7)));
        assertFalse(new Interval<>(valueOf(5), false, valueOf(7), true).contains(valueOf(7)));
        assertTrue(new Interval<>(valueOf(5), false, valueOf(7), false).contains(valueOf(7)));
    }

    @Test
    void testToString() {
        assertEquals("]5, 7[", new Interval<>(valueOf(5), true, valueOf(7), true).toString());
        assertEquals("]5, 7]", new Interval<>(valueOf(5), true, valueOf(7), false).toString());
        assertEquals("[5, 7[", new Interval<>(valueOf(5), false, valueOf(7), true).toString());
        assertEquals("[5, 7]", new Interval<>(valueOf(5), false, valueOf(7), false).toString());
    }
}