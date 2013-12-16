package org.slevental.anaphora.core.algs;

/*************************************************************************
 *  Compilation:  javac org.eslion.Copy.java
 *  Execution:    java org.eslion.Copy < file
 *  Dependencies: org.eslion.BinaryStdIn.java org.eslion.BinaryStdOut.java
 *  
 *  Reads in a binary file from standard input and writes it to standard output.
 *
 *  % java org.eslion.Copy < mandrill.jpg > copy.jpg
 *
 *  %  diff mandrill.jpg copy.jpg
 *
 *************************************************************************/

public class Copy {

    public static void main(String[] args) {
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(c);
        }
        BinaryStdOut.flush();
    }
}
