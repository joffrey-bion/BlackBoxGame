package com.jbion.games.blackbox;

public class EntropyTest {
    private static final double log2 = Math.log(2);

    public static void main(String[] args) {
        printCompare(4, 4, 4, 4);
        printCompare(3, 4, 4, 5);
        printCompare(2, 4, 4, 6);
        printCompare(1, 4, 4, 7);
        printCompare(1, 3, 4, 8);
        printCompare(1, 2, 4, 9);
        printCompare(1, 1, 4, 10);
        printCompare(1, 1, 3, 11);
        printCompare(1, 1, 2, 12);
        printCompare(1, 1, 1, 13);
        printCompare(0, 0, 0, 16);
        System.out.println();
        printCompare(3, 3, 3);
        printCompare(2, 3, 4);
        printCompare(1, 3, 5);
        printCompare(1, 2, 6);
        printCompare(1, 1, 7);
    }

    private static void printCompare(int... nvs) {
        System.out.println("avg rem = " + squareSum(nvs) + "\tIG = " + IG(nvs));
    }

    /**
     * Returns the entropy of a distribution of probabilities.
     * 
     * @param ps
     *            The probabilities in the distribution.
     * @return The entropy of the distribution {@code ps}.
     */
    private static double I(double... ps) {
        double sum = 0;
        for (double p : ps) {
            if (p != 0) {
                sum += -p * Math.log(p) / log2;
            }
        }
        return sum;
    }

    public static double squareSum(int... nvs) {
        double sum = 0;
        double sumSq = 0;
        for (int nv : nvs) {
            sum += nv;
            sumSq += nv * nv;
        }
        return sumSq / sum;
    }

    private static double IG(int... nvs) {
        return ITotal(nvs) - remainder(nvs);
    }

    private static double ITotal(int... nvs) {
        double sum = 0;
        for (int nv : nvs) {
            sum += nv;
        }
        double p = 1 / sum;
        double n = (sum - 1) / sum;
        return I(p, n);
    }

    public static double remainder(int... nvs) {
        double sum = 0;
        double rem = 0;
        for (int nv : nvs) {
            sum += nv;
            if (nv != 0) {
                double p = (double) 1 / (double) nv;
                double n = (double) (nv - 1) / (double) nv;
                rem += nv * I(p, n);
            }
        }
        return rem / sum;
    }
}
