package name.julatec.util.collection;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.math.BigInteger.valueOf;
import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.*;

class SortMergeJoinTest {

    private SortMergeJoin<Pair<String, Integer>, Pair<String, BigInteger>, Triple<Integer, String, BigInteger>> merger =
            SortMergeJoin.of(
                    (l, r) -> l.getKey().compareTo(r.getKey()),
                    (m, l) -> m.map(v -> Triple.of(l.getValue(), v.getMiddle(), v.getRight()))
                            .orElse(Triple.of(l.getValue(), l.getKey(), null)),
                    (m, r) -> m.map(v -> Triple.of(v.getLeft(), v.getMiddle(), r.getRight()))
                            .orElse(Triple.of(null, r.getKey(), r.getRight())),
                    comparing(Pair::getKey),
                    comparing(Pair::getKey));

    @Test
    void merge() {
        final Stream<Pair<String, Integer>> left = Arrays.asList(
                Pair.of("A", 1),
                Pair.of("C", 2),
                Pair.of("E", 2)
        ).stream();
        final Stream<Pair<String, BigInteger>> right = Arrays.asList(
                Pair.of("A", valueOf(1)),
                Pair.of("C", valueOf(3)),
                Pair.of("E", valueOf(1))
        ).stream();
        final List<Triple<Integer, String, BigInteger>> result = merger.merge(left, right).collect(Collectors.toList());
        assertArrayEquals(
                new Triple[]{
                        Triple.of(1, "A", valueOf(1)),
                        Triple.of(2, "C", valueOf(3)),
                        Triple.of(2, "E", valueOf(1)),
                },
                result.toArray());
    }

    @Test
    void merge2() {
        final Stream<Pair<String, Integer>> left = Arrays.asList(
                Pair.of("B", 1),
                Pair.of("C", 2),
                Pair.of("E", 2)
        ).stream();
        final Stream<Pair<String, BigInteger>> right = Arrays.asList(
                Pair.of("A", valueOf(1)),
                Pair.of("B", valueOf(3)),
                Pair.of("D", valueOf(1))
        ).stream();
        final List<Triple<Integer, String, BigInteger>> result = merger.merge(left, right).collect(Collectors.toList());
        assertArrayEquals(
                new Triple[]{
                        Triple.of(null, "A", valueOf(1)),
                        Triple.of(1, "B", valueOf(3)),
                        Triple.of(2, "C", null),
                        Triple.of(null, "D", valueOf(1)),
                        Triple.of(2, "E", null),
                },
                result.toArray());
    }
}