package name.julatec.util.statistics;

import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Produces an infinite series fo normal distributed groups.
 * The number groups varies from 5 tp 15. Each number has it own variance, but all of them
 * are centered at 0.
 */
public class RandomGroupGenerator {

    public static void main(String[] args) throws InterruptedException {
        final Random groupRandom = new Random();
        final int groups = 5 + groupRandom.nextInt(10);
        final Random[] randoms = new Random[groups];
        final double[] sigmas = new double[groups];
        for (int i = 0; i < groups; i++) {
            randoms[i] = new Random();
            sigmas[i] = groupRandom.nextGaussian() * 100;
        }
        while (true) {
            final int group = groupRandom.nextInt(groups);
            final double value = randoms[group].nextGaussian() * sigmas[group];
            final long longValue = (long) value;
            System.out.println(String.format("G%03d %20d", group, longValue));
            sleep(300);
        }
    }

}
