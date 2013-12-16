package org.slevental.anaphora.core.wordnet;

import org.slevental.anaphora.core.algs.BreadthFirstDirectedPaths;
import org.slevental.anaphora.core.algs.Digraph;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph digraph) {
        this.digraph = new Digraph(digraph);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return new Ancestor(digraph, v, w).pathLen;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return new Ancestor(digraph, v, w).v;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return new Ancestor(digraph, v, w).pathLen;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return new Ancestor(digraph, v, w).v;
    }

    private static class Ancestor {
        int pathLen = -1;
        int v = -1;

        Ancestor() {
        }

        Ancestor(Digraph g, int v, int w) {
            BreadthFirstDirectedPaths vbfs = new BreadthFirstDirectedPaths(g, v);
            BreadthFirstDirectedPaths wbfs = new BreadthFirstDirectedPaths(g, w);
            init(g, vbfs, wbfs);
        }

        Ancestor(Digraph g, Iterable<Integer> v, Iterable<Integer> w) {
            BreadthFirstDirectedPaths vbfs = new BreadthFirstDirectedPaths(g, v);
            BreadthFirstDirectedPaths wbfs = new BreadthFirstDirectedPaths(g, w);
            init(g, vbfs, wbfs);
        }

        private void init(Digraph graph, BreadthFirstDirectedPaths vbfs, BreadthFirstDirectedPaths wbfs) {
            for (int each = 0; each < graph.V(); each++) {
                if (vbfs.hasPathTo(each) &&
                        wbfs.hasPathTo(each) &&
                        (pathLen < 0 || vbfs.distTo(each) + wbfs.distTo(each) < pathLen)) {
                    pathLen = vbfs.distTo(each) + wbfs.distTo(each);
                    this.v = each;
                }
            }
        }
    }
}
