package org.slevental.anaphora.core.algs;

/*************************************************************************
 *  Compilation:  javac org.eslion.Average.java
 *  Execution:    java org.eslion.Average < data.txt
 *  Dependencies: org.eslion.StdIn.java org.eslion.StdOut.java
 *  
 *  Reads in a sequence of real numbers, and computes their average.
 *
 *  % java org.eslion.Average
 *  10.0 5.0 6.0
 *  3.0 7.0 32.0
 *  <Ctrl-d>
 *  org.eslion.Average is 10.5

 *  Note <Ctrl-d> signifies the end of file on Unix.
 *  On windows use <Ctrl-z>.
 *
 *************************************************************************/

public class Average { 
    public static void main(String[] args) { 
        int count = 0;       // number input values
        double sum = 0.0;    // sum of input values

        // read data and compute statistics
        while (!StdIn.isEmpty()) {
            double value = StdIn.readDouble();
            sum += value;
            count++;
        }

        // compute the average
        double average = sum / count;

        // print results
        StdOut.println("org.eslion.Average is " + average);
    }
}
