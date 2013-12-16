package org.slevental.anaphora.core.algs;

/*************************************************************************
 *  Compilation:  javac org.eslion.LRS.java
 *  Execution:    java org.eslion.LRS < file.txt
 *  Dependencies: org.eslion.StdIn.java org.eslion.SuffixArray.java
 *  Data files:   http://algs4.cs.princeton.edu/63suffix/tinyTale.txt
 *                http://algs4.cs.princeton.edu/63suffix/mobydick.txt
 *  
 *  Reads a text string from stdin, replaces all consecutive blocks of
 *  whitespace with a single space, and then computes the longest
 *  repeated substring in that text using a suffix array.
 * 
 *  % java org.eslion.LRS < tinyTale.txt
 *  'st of times it was the '
 *
 *  % java org.eslion.LRS < mobydick.txt
 *  ',- Such a funny, sporty, gamy, jesty, joky, hoky-poky lad, is the Ocean, oh! Th'
 * 
 *  % java org.eslion.LRS
 *  aaaaaaaaa
 *  'aaaaaaaa'
 *
 *  % java org.eslion.LRS
 *  abcdefg
 *  ''
 *
 *************************************************************************/


public class LRS {

    public static void main(String[] args) {
        String text = StdIn.readAll().replaceAll("\\s+", " ");
        SuffixArray sa = new SuffixArray(text);

        int N = sa.length();

        String lrs = "";
        for (int i = 1; i < N; i++) {
            int length = sa.lcp(i);
            if (length > lrs.length())
                lrs = sa.select(i).substring(0, length);
        }
        
        StdOut.println("'" + lrs + "'");
    }
}
