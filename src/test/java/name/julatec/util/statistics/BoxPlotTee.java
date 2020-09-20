package name.julatec.util.statistics;

import name.julatec.util.algebraic.Interval;

import java.io.PrintStream;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.String.format;

public class BoxPlotTee {

    private static <T> Consumer<T> printTee(
            BoxPlot<String, Long> boxPlotTee,
            PrintStream out,
            Long millis,
            Long rows) {
        return new Consumer<T>() {

            private long lastPrintTime = System.currentTimeMillis();
            private long lines = 0;

            @Override
            public void accept(T t) {
                final long now = System.currentTimeMillis();
                if (now - lastPrintTime > millis || ++lines % rows == 0) {
                    lastPrintTime = now;
                    print();
                }
            }

            private void print() {
                final int columns = Optional.ofNullable(System.getenv("COLUMNS"))
                        .map(Integer::valueOf)
                        .orElse(80);
                final Interval<Integer> consoleInterval = Interval.of(10, columns - 5);
                final FiveNumberSummary<Long> globalSummary = boxPlotTee.getSummary().get();
                final NavigableMap<String, Optional<FiveNumberSummary<Long>>> summaries = boxPlotTee.getSummaries();
                final Interval<Long> interval = Interval.of(globalSummary.min, globalSummary.max);
                int row = 1;
                moveTo(0, 0);
                final String header = format(format("Groups  %%5d%%%ds%%5d", columns - 20),
                        interval.lower, "", interval.upper);
                final Function<Long, Integer> consoleScale2 =
                        Interval.scale(interval, consoleInterval, Number::intValue);
                out.println(header);
                for (Map.Entry<String, Optional<FiveNumberSummary<Long>>> entry : summaries.entrySet()) {
                    printBox(entry.getKey(),
                            entry.getValue().get().map(consoleScale2),
                            columns,
                            ++row);
                }
                printBox("", globalSummary.map(consoleScale2), columns, ++row);
                out.println(header);
            }

            private void printBox(
                    String group,
                    FiveNumberSummary<Integer> scaled,
                    int columns,
                    int row) {
                moveTo(row, 0);
                out.print(format("%9s ", group));
                int column = 10;
                while (column++ < scaled.min) out.print(' ');
                out.print('|');
                while (column++ < scaled.lowerQuartile) out.print('-');
                out.print('[');
                while (column++ < scaled.median) out.print('=');
                out.print('+');
                while (column++ < scaled.upperQuartile) out.print('=');
                out.print(']');
                while (column++ < scaled.max) out.print('-');
                out.print('|');
                while (column++ < columns) out.print(' ');
                out.println();
            }

            private void moveTo(int row, int column) {
                char escCode = 0x1B;
                out.print(format("%c[%d;%df", escCode, row, column));
            }
        };

    }

    static Map.Entry<String, Long> getDuration(String line) {
        final String[] parts = line.split(" +");
        return Map.entry(parts[0], Long.valueOf(parts[1]));
    }

    public static void main(String[] args) {
        final BoxPlot<String, Long> boxPlotTee = new BoxPlot<>();
        Stream.generate(new Scanner(System.in)::nextLine)
                .peek(boxPlotTee.tee(BoxPlotTee::getDuration))
                .peek(printTee(boxPlotTee, System.err, 2000l, 1000l))
                .forEach(System.out::println);
    }
}
