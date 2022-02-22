package lab12;

import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Mean {
    static double[] array;
    static BlockingQueue<Double> results = new ArrayBlockingQueue<>(100);
    static Semaphore sem = new Semaphore(0);

    static void initArray(int size) {
        array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = Math.random() * size / (i + 1);
        }
    }

    public static void main(String[] args) {
        initArray(100000000);
        parallelMean(100);
    }

    /**
     * Oblicza średnią wartości elementów tablicy array uruchamiając równolegle działające wątki.
     * Wypisuje czasy operacji
     *
     * @param cnt - liczba wątków
     */
    static void parallelMean(int cnt) {
        // utwórz tablicę wątków
        MeanCalc[] threads = new MeanCalc[cnt];

        // utwórz wątki, podziel tablice na równe bloki i przekaż indeksy do wątków
        // załóż, że array.length dzieli się przez cnt)
        for (int i = 0; i < cnt; ) {
            int blockSize = array.length / cnt;
            threads[i] = new MeanCalc(i * blockSize, (++i) * blockSize);
        }
        double t1 = System.nanoTime() / 1e6;

        // uruchom wątki
        for (MeanCalc meanCalc : threads) {
            meanCalc.start();
        }
        double t2 = System.nanoTime() / 1e6;

        // use semaphore instead
        // // czekaj na ich zakończenie używając metody "join"
        // for (MeanCalc meanCalc : threads) {
        //     try {
        //         meanCalc.join();
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
        try {
            sem.acquire(cnt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // oblicz średnią ze średnich
        double mean = 0;
        for (MeanCalc meanCalc : threads) {
            try {
                mean += results.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mean /= cnt;

        double t3 = System.nanoTime() / 1e6;
        System.out.printf(Locale.US, "size = %d cnt=%d >  t2-t1=%f t3-t1=%f mean=%f\n",
                array.length,
                cnt,
                t2 - t1,
                t3 - t1,
                mean);
    }

    static class MeanCalc extends Thread {
        private final int start;
        private final int end;
        double mean = 0;

        MeanCalc(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public void run() {
            for (int i = start; i < end; i++) {
                mean += array[i];
            }
            mean /= end - start;
            try {
                results.put(mean);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf(Locale.US, "%d-%d mean=%f\n", start, end, mean);

            sem.release();
        }
    }
}
