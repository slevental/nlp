package org.slevental.anaphora.core.wordnet;

import org.slevental.anaphora.core.algs.Digraph;
import org.slevental.anaphora.core.algs.DirectedCycle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordNet {
    private final Map<String, Collection<Synset>> synsetsIndexByNoun = new HashMap<String, Collection<Synset>>();
    private final Map<Integer, Synset> synsetsIndexById = new HashMap<Integer, Synset>();
    private final Digraph digraph;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        try {
            BufferedReader synsetsReader = new BufferedReader(new FileReader(new File(synsets)));
            BufferedReader hypernymsReader = new BufferedReader(new FileReader(new File(hypernyms)));
            digraph = createGraph(synsetsReader);
            fillGraph(hypernymsReader);
            DirectedCycle cycle = new DirectedCycle(digraph);
            if (cycle.hasCycle()) {
                throw new IllegalArgumentException();
            }
            sap = new SAP(digraph);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        List<String> res = new ArrayList<String>();
        for (Synset each : synsetsIndexById.values()) {
            res.addAll(Arrays.asList(each.synset.split(" ")));
        }
        return res;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetsIndexByNoun.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validate(nounA, nounB);
        return sap.length(id(nounA), id(nounB));
    }

    private ArrayList<Integer> id(String noun) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        for (Synset e : synsetsIndexByNoun.get(noun)) {
            res.add(e.id);
        }
        return res;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validate(nounA, nounB);
        int ancestor = sap.ancestor(id(nounA), id(nounB));
        return ancestor < 0 ? null : synsetsIndexById.get(ancestor).synset;
    }

    private void validate(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
    }

    private void fillGraph(BufferedReader hypernymsReader) throws IOException {
        String line;
        while ((line = hypernymsReader.readLine()) != null) {
            String[] split = line.split(",");
            for (int i = 1; i < split.length; i++) {
                this.digraph.addEdge(
                        Integer.valueOf(split[0]),
                        Integer.valueOf(split[i])
                );
            }
        }
    }

    private Digraph createGraph(BufferedReader synsetsReader) throws IOException {
        String line;
        while ((line = synsetsReader.readLine()) != null) {
            String[] split = line.split(",");
            Integer externalId = Integer.valueOf(split[0].trim());
            Synset set = new Synset(externalId, split[1], split[2]);
            this.synsetsIndexById.put(externalId, set);
            for (String noun : split[1].split(" ")) {
                Collection<Synset> synsets = synsetsIndexByNoun.get(noun);
                if (synsets == null) {
                    synsets = new ArrayList<Synset>();
                    synsetsIndexByNoun.put(noun, synsets);
                }
                synsets.add(set);
            }
        }
        return new Digraph(synsetsIndexById.size() + 1);
    }

    private static class Synset {
        final int id;
        final String synset;
        final String gloss;

        private Synset(int id, String synset, String gloss) {
            this.id = id;
            this.synset = synset;
            this.gloss = gloss;
        }
    }
}
