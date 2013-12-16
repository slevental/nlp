package org.slevental.anaphora.core.algs;

/*************************************************************************
 *  Compilation:  javac org.eslion.NFA.java
 *  Execution:    java org.eslion.NFA regexp text
 *  Dependencies: Stack.java org.eslion.Bag.java org.eslion.Digraph.java org.eslion.DirectedDFS.java
 *
 *  % java org.eslion.NFA "(A*B|AC)D" AAAABD
 *  true
 *
 *  % java org.eslion.NFA "(A*B|AC)D" AAAAC
 *  false
 *
 *  % java org.eslion.NFA "(a|(bc)*d)*" abcbcd
 *  true
 *
 *  % java org.eslion.NFA "(a|(bc)*d)*" abcbcbcdaaaabcbcdaaaddd
 *  true
 *
 *************************************************************************/

public class NFA { 

    private Digraph G;         // digraph of epsilon transitions
    private String regexp;     // regular expression
    private int M;             // number of characters in regular expression

    // Create the org.eslion.NFA for the given RE
    public NFA(String regexp) {
        this.regexp = regexp;
        M = regexp.length();
        Stack<Integer> ops = new Stack<Integer>(); 
        G = new Digraph(M+1); 
        for (int i = 0; i < M; i++) { 
            int lp = i; 
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '|') 
                ops.push(i); 
            else if (regexp.charAt(i) == ')') {
                int or = ops.pop(); 
                if (regexp.charAt(or) == '|') { 
                    lp = ops.pop();
                    G.addEdge(lp, or+1);
                    G.addEdge(or, i);
                }
                else if (regexp.charAt(or) == '(')
                    lp = or;
                else assert false;
            } 

            // Lookahead;  
            if (i < M-1 && regexp.charAt(i+1) == '*') { 
                G.addEdge(lp, i+1); 
                G.addEdge(i+1, lp); 
            } 
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '*' || regexp.charAt(i) == ')') 
                G.addEdge(i, i+1);
        } 
    } 

    // Does the org.eslion.NFA recognize txt?
    public boolean recognizes(String txt) {
        DirectedDFS dfs = new DirectedDFS(G, 0);
        Bag<Integer> pc = new Bag<Integer>();
        for (int v = 0; v < G.V(); v++)
            if (dfs.marked(v)) pc.add(v);

        // Compute possible org.eslion.NFA states for txt[i+1]
        for (int i = 0; i < txt.length(); i++) {
            Bag<Integer> match = new Bag<Integer>();
            for (int v : pc) {
                if (v == M) continue;
                if ((regexp.charAt(v) == txt.charAt(i)) || regexp.charAt(v) == '.')
                    match.add(v+1); 
            }
            dfs = new DirectedDFS(G, match); 
            pc = new Bag<Integer>();
            for (int v = 0; v < G.V(); v++)
                if (dfs.marked(v)) pc.add(v);

            // optimization if no states reachable
            if (pc.size() == 0) return false;
        }

        // check for accept state
        for (int v : pc)
            if (v == M) return true;
        return false;
    }


    public static void main(String[] args) {
        String regexp = "(" + args[0] + ")";
        String txt = args[1];
        NFA nfa = new NFA(regexp);
        StdOut.println(nfa.recognizes(txt));
    }

} 
