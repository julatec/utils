package name.julatec.util.statistics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class BoxPlotTest {

    Stream<String> lines;

    @BeforeEach
    void setUp() {
        final InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("name/julatec/util/statistics/data.txt");
        final Scanner scanner = new Scanner(inputStream);
        Iterator<String> sourceIterator = new Iterator<>() {
            @Override
            public boolean hasNext() {
                return scanner.hasNext();
            }

            @Override
            public String next() {
                return scanner.nextLine();
            }
        };
        lines = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(sourceIterator, Spliterator.ORDERED),
                false);
    }

    @Test
    void test() {
        BoxPlot<String, Long> boxPlot = new BoxPlot<>();
        lines.peek(boxPlot.tee(BoxPlotTee::getDuration))
                .forEach(s -> {
                });
        Optional<FiveNumberSummary<Long>> summaryOptional = boxPlot.getSummary();
        assertTrue(summaryOptional.isPresent());
        assertEquals("[-504, -48, 0, 48, 602]", summaryOptional.get().toString());
        NavigableMap<String, Optional<FiveNumberSummary<Long>>> groupSummaries = boxPlot.getSummaries();
        assertEquals(8, groupSummaries.size());
        assertEquals("[-260, -51, 0, 52, 245]",
                groupSummaries.get("G000").map(FiveNumberSummary::toString).orElse(""));
        assertEquals("[-418, -89, -3, 87, 368]",
                groupSummaries.get("G001").map(FiveNumberSummary::toString).orElse(""));
        assertEquals("[-116, -24, -1, 21, 90]",
                groupSummaries.get("G002").map(FiveNumberSummary::toString).orElse(""));
        assertEquals("[-334, -73, -1, 71, 327]",
                groupSummaries.get("G003").map(FiveNumberSummary::toString).orElse(""));
        assertEquals("[-214, -49, 0, 50, 291]",
                groupSummaries.get("G004").map(FiveNumberSummary::toString).orElse(""));
        assertEquals("[-455, -99, 3, 106, 445]",
                groupSummaries.get("G005").map(FiveNumberSummary::toString).orElse(""));
        assertEquals("[-49, -11, 0, 10, 50]",
                groupSummaries.get("G006").map(FiveNumberSummary::toString).orElse(""));
        assertEquals("[-504, -98, -4, 101, 602]",
                groupSummaries.get("G007").map(FiveNumberSummary::toString).orElse(""));
    }

    @AfterEach
    void tearDown() {
    }
}