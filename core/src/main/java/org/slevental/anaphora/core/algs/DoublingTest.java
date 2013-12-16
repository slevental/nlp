package org.slevental.anaphora.core.algs;

/*************************************************************************
 *  Compilation:  javac org.eslion.DoublingTest.java
 *  Execution:    java org.eslion.DoublingTest
 *  Dependencies: org.eslion.ThreeSum.java org.eslion.Stopwatch.java org.eslion.StdRandom.java org.eslion.StdOut.java
 *
 *  % java org.eslion.DoublingTest
 *      250   0.0
 *      500   0.0
 *     1000   0.1
 *     2000   0.6
 *     4000   4.5
 *     8000  35.7
 *  ...
 *
 *************************************************************************/

public class DoublingTest {

    // time org.eslion.ThreeSum.count() for N random 6-digit ints
    public static double timeTrial(int N) {
        int MAX = 1000000;
        int[] a = new int[N];
        for (int i = 0; i < N; i++) {
            a[i] = StdRandom.uniform(-MAX, MAX);
        }
        Stopwatch timer = new Stopwatch();
        int cnt = ThreeSum.count(a);
        return timer.elapsedTime();
    }


    public static void main(String[] args) { 
        for (int N = 250; true; N += N) {
            double time = timeTrial(N);
            StdOut.printf("%7d %5.1f\n", N, time);
        } 
    } 
} 

