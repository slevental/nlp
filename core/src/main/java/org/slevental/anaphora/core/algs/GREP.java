package org.slevental.anaphora.core.algs;

/*************************************************************************
 *  Compilation:  javac org.eslion.GREP.java
 *  Execution:    java org.eslion.GREP regexp < input.txt
 *  Dependencies: org.eslion.NFA.java
 *  Data files:   http://algs4.cs.princeton.edu/54regexp/tinyL.txt
 *
 *  This program takes an RE as a command-line argument and prints
 *  the lines from standard input having some substring that
 *  is in the language described by the RE. 
 *
 *  % more tinyL.txt
 *  AC
 *  AD
 *  AAA
 *  ABD
 *  ADD
 *  BCD
 *  ABCCBD
 *  BABAAA
 *  BABBAAA
 *
 *  %  java org.eslion.GREP "(A*B|AC)D" < tinyL.txt
 *  ABD
 *  ABCCBD
 *
 *************************************************************************/

public class GREP {
    public static void main(String[] args) { 
        String regexp = "(.*" + args[0] + ".*)";
        NFA nfa = new NFA(regexp);
        while (StdIn.hasNextLine()) { 
            String txt = StdIn.readLine();
            if (nfa.recognizes(txt)) {
                StdOut.println(txt);
            }
        }
    } 
} 
