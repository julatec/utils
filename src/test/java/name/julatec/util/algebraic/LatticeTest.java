package name.julatec.util.algebraic;

import org.junit.jupiter.api.Test;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static name.julatec.util.algebraic.LatticeTest.Letters.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LatticeTest {

    enum Letters {
        A,
        B,
        C,
        D,
        E
    }

    private static final Lattice<Letters> lattice = Letters::compareTo;

    @Test
    void max() {
        assertEquals(A, lattice.max(A, A));
        assertEquals(B, lattice.max(A, B));
        assertEquals(B, lattice.max(B, A));
    }

    @Test
    void min() {
        assertEquals(C, lattice.min(C, C));
        assertEquals(C, lattice.min(C, D));
        assertEquals(C, lattice.min(D, C));
    }

    @Test
    void testMax() {
        assertEquals(empty(), lattice.max(empty(), empty()));
        assertEquals(of(E), lattice.max(empty(), of(E)));
        assertEquals(of(E), lattice.max(of(E), empty()));
        assertEquals(of(E), lattice.max(of(E), of(E)));
        assertEquals(of(E), lattice.max(of(A), of(E)));
        assertEquals(of(E), lattice.max(of(E), of(A)));
    }

    @Test
    void testMin() {
        assertEquals(empty(), lattice.min(empty(), empty()));
        assertEquals(of(E), lattice.min(empty(), of(E)));
        assertEquals(of(E), lattice.min(of(E), empty()));
        assertEquals(of(E), lattice.min(of(E), of(E)));
        assertEquals(of(A), lattice.min(of(A), of(E)));
        assertEquals(of(A), lattice.min(of(E), of(A)));
    }
}