package org.slevental.anaphora.core.algs;

/*************************************************************************
 *  Compilation:  javac org.eslion.BlackFilter.java
 *  Execution:    java org.eslion.BlackFilter blacklist.txt < input.txt
 *  Dependencies: org.eslion.SET In.java org.eslion.StdIn.java org.eslion.StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/35applications/tinyTale.txt
 *                http://algs4.cs.princeton.edu/35applications/list.txt
 *
 *  Read in a blacklist of words from a file. Then read in a list of
 *  words from standard input and print out all those words that
 *  are not in the first file.
 * 
 *  % more tinyTale.txt 
 *  it was the best of times it was the worst of times 
 *  it was the age of wisdom it was the age of foolishness 
 *  it was the epoch of belief it was the epoch of incredulity 
 *  it was the season of light it was the season of darkness 
 *  it was the spring of hope it was the winter of despair
 *
 *  % more list.txt 
 *  was it the of 
 * 
 *  % java org.eslion.BlackFilter list.txt < tinyTale.txt
 *  best times worst times 
 *  age wisdom age foolishness 
 *  epoch belief epoch incredulity 
 *  season light season darkness 
 *  spring hope winter despair 
 *
 *************************************************************************/

public class BlackFilter {  
    public static void main(String[] args) {
        SET<String> set = new SET<String>();

        // read in strings and add to set
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            String word = in.readString();
            set.add(word);
        }

        // read in string from standard input, printing out all exceptions
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (!set.contains(word))
                StdOut.println(word);
        }
    }
}
