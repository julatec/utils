package name.julatec.util.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;

class LookAheadIteratorTest {


    private static final Iterable<String> list = Arrays.asList("a", "b", "c");
    private LookAheadIterator<String> iterator;

    @BeforeEach
    void init() {
        iterator = new LookAheadIterator<>(list.iterator());
    }

    @Test
    void nextIteration() {
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertEquals(ofNullable("b"), iterator.peek());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertEquals(ofNullable("c"), iterator.peek());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertEquals(Optional.empty(), iterator.peek());
        assertFalse(iterator.hasNext());
    }
}