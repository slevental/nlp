package org.slevental.anaphora.core.triplet;

import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import gate.creole.ontology.Triple;
import opennlp.tools.parser.Parse;
import opennlp.tools.util.Span;
import org.slevental.anaphora.core.annotator.AbstractAnnotator;
import org.slevental.anaphora.core.annotator.AnnotationException;
import org.slevental.anaphora.core.opennlp.OpenNLPParser;
import org.slevental.anaphora.core.txt.Annotation;
import org.slevental.anaphora.core.txt.AnnotationType;
import org.slevental.anaphora.core.txt.StaticFeature;
import org.slevental.anaphora.core.txt.Text;

import java.util.*;
import java.util.regex.Pattern;

import static com.google.common.base.Predicates.*;


/**
 * Triplet extractor based on OpenNLP parser
 */
public class TripletExtractor extends AbstractAnnotator<Text> {
    public static final int NUM_PARSES = 1;
    public static final Predicate<Parse> SUBJ_OBJ_TYPES = regexType("(NN(P|PS|S)?|PRPR?$?|VBG)");
    public static final Predicate<Parse> NOUN_PRASE = or(regexType("NP"), regexLabel("S-S"));

    private static final Ordering<Parse> offsetOrder = Ordering.from(new Comparator<Parse>() {
        @Override
        public int compare(Parse o1, Parse o2) {
            return ComparisonChain.start()
                    .compare(o1.getSpan().getStart(), o2.getSpan().getStart())
                    .compare(o1.getSpan().getEnd(), o2.getSpan().getEnd())
                    .compare(o1.getType(), o2.getType())
                    .compare(o1.getChildCount(), o2.getChildCount())
                    .result();
        }
    });

    private final OpenNLPParser parser;
    private boolean debugMode = false;


    public TripletExtractor(OpenNLPParser parser, boolean debug) {
        this.parser = parser;
        this.debugMode = debug;
    }

    public TripletExtractor(boolean debug) {
        this(new OpenNLPParser(), debug);
    }

    public TripletExtractor() {
        this(false);
    }

    @Override
    public Text annotate(Text in) throws AnnotationException {
        for (Annotation sentence : in.getByType(AnnotationType.Sentence)) {
            String originalText = in.getText(sentence);
            Triplet triplet = extract(originalText);
            addAnnotation(in, sentence.getLo(), triplet.getObject(), AnnotationType.Object);
            addAnnotation(in, sentence.getLo(), triplet.getPredicate(), AnnotationType.Predicate);
            addAnnotation(in, sentence.getLo(), triplet.getSubject(), AnnotationType.Subject);
        }
        return in;
    }

    public Triplet extract(String originalText) {
        String normalizedText = originalText.replaceAll("(\\.|\\?|!|\\)|\\()", " ");
        Parse tree = parser.parse(normalizedText, 1)[0];
        Parse subject;
        Parse predicate;
        Parse object;
        Parse npTree = bfs(NOUN_PRASE, tree);
        Parse vpTree = bfs(eqType("VP"), tree);

        subject = findSubject(npTree);
        predicate = findPredicate(vpTree);
        Parse predicateVp = findParent(predicate, eqType("VP"));
        object = findObject(predicateVp);
        if (object == null) {
            Predicate<Parse> deepestNonSubjNN = and(SUBJ_OBJ_TYPES, not(equalTo(subject)));
            object = deepest(ImmutableList.of(tree), null, deepestNonSubjNN);
        }

        if (debugMode) {
            System.err.println(String.format("%s -> %s -> %s",
                    subject == null ? "NOP" : getText(normalizedText, subject),
                    predicate == null ? "NOP" : getText(normalizedText, predicate),
                    object == null ? "NOP" : getText(normalizedText, object)));
        }
        return new Triplet(subject, object, predicate);
    }

    private CharSequence getText(String text, Parse predicate) {
        Span span = predicate.getSpan();

        return text.substring(
                Math.max(0, span.getStart()),
                Math.min(text.length(), span.getEnd())
        );
    }

    private void addAnnotation(Text in, int offset, Parse object, AnnotationType type) {
        if (object == null)
            return;

        int len = in.getText().length();
        int lo = object.getSpan().getStart() + offset;
        int hi = Math.min(object.getSpan().getEnd() + offset, len);
        in.addAnnotation(Annotation.builder()
                .interval(lo, hi)
                .type(type)
                .feature(StaticFeature.string, in.getText(lo, hi))
                .build());
    }

    private Iterable<Parse> findAll(List<Parse> tree, Predicate<Parse> predicate) {
        List<Parse> res = new ArrayList<Parse>();
        getAll(res, tree);
        return Iterables.filter(res, predicate);
    }

    private void getAll(List<Parse> aux, List<Parse> tree) {
        for (Parse parse : tree) {
            aux.add(parse);
            getAll(aux, Arrays.asList(parse.getChildren()));
        }
    }

    private Parse findSubject(Parse npTree) {
        return npTree == null ? null : bfs(SUBJ_OBJ_TYPES, npTree.getChildren());
    }

    private Parse findPredicate(Parse vpTree) {
        return vpTree == null ? null : deepest(Arrays.asList(vpTree.getChildren()), null, regexType("VB.?"));
    }

    private Parse findObject(Parse predicateVp) {
        if (predicateVp == null) return null;

        Parse npPhrase = bfs(eqType("NP"), predicateVp);
        Parse ppPhrase = bfs(eqType("PP"), predicateVp);
        Parse adjPhrase = bfs(eqType("ADJP"), predicateVp);

        Parse npCandidate = npPhrase != null ? findObjectInPhrase(npPhrase) : null;
        Parse ppCandidate = ppPhrase != null ? findObjectInPhrase(ppPhrase) : null;
        Parse adjCandidate = adjPhrase != null ? bfs(regexType("JJ.?"), adjPhrase) : null;
        return firstNonNull(npCandidate, ppCandidate, npPhrase, adjCandidate, ppPhrase, adjPhrase);
    }

    private Parse findObjectInPhrase(Parse npPhrase) {
        List<Parse> objList = bfs(true, eqType("NNP"), npPhrase);
        if (objList.isEmpty()) {
            return bfs(SUBJ_OBJ_TYPES, npPhrase);
        } else {
            return combine(objList);
        }
    }

    private Parse combine(List<Parse> objList) {
        //order before combine
        objList = offsetOrder.sortedCopy(objList);

        Parse first = Iterables.getFirst(objList, null);
        Parse last = Iterables.getLast(objList, null);
        return new Parse(first.getText(), new Span(first.getSpan().getStart(), last.getSpan().getEnd()), first.getType(), 0d, 0);
    }

    private Parse firstNonNull(Parse... parses) {
        for (Parse each : parses) {
            if (each != null)
                return each;
        }
        return null;
    }

    private static Parse findFirst(Parse p, Predicate<Parse> predicate) {
        if (predicate.apply(p)) return p;
        for (Parse each : p.getChildren()) {
            Parse res = findFirst(each, predicate);
            if (res != null) return res;
        }
        return null;
    }

    private static Parse findParent(Parse p, Predicate<Parse> predicate) {
        if (p == null) return null;
        if (predicate.apply(p)) return p;
        return findParent(p.getParent(), predicate);
    }

    private static Predicate<Parse> eqType(final String type) {
        return new Predicate<Parse>() {
            @Override
            public boolean apply(Parse input) {
                return input.getType().equals(type);
            }
        };
    }

    private static Predicate<Parse> regexType(final String type) {
        final Pattern p = Pattern.compile(type);
        return new Predicate<Parse>() {
            @Override
            public boolean apply(Parse input) {
                return p.matcher(input.getType()).matches();
            }
        };
    }

    private static Predicate<Parse> regexLabel(final String type) {
        final Pattern p = Pattern.compile(type);
        return new Predicate<Parse>() {
            @Override
            public boolean apply(Parse input) {
                return input.getLabel() != null && p.matcher(input.getLabel()).matches();
            }
        };
    }

    private static List<Parse> bfs(boolean greedy, Predicate<Parse> predicate, Parse... p) {
        List<Parse> res = new ArrayList<Parse>();
        Queue<Parse> queue = new LinkedList<Parse>(Arrays.asList(p));
        while (!queue.isEmpty()) {
            Parse e = queue.poll();
            if (predicate.apply(e)) {
                if (!greedy) {
                    res.add(e);
                    return res;
                }
                Parse prev = Iterables.getLast(res, null);
                if (prev == null || prev.getType().equalsIgnoreCase(e.getType())) {
                    res.add(e);
                } else {
                    return res;
                }
            }
            queue.addAll(Arrays.asList(e.getChildren()));
        }
        return res;
    }

    private static Parse bfs(Predicate<Parse> predicate, Parse... p) {
        return Iterables.getOnlyElement(bfs(false, predicate, p), null);
    }

    private static Parse deepest(List<Parse> p, Parse candidate, Predicate<Parse> predicate) {
        if (p.isEmpty()) return candidate;

        List<Parse> nextLayer = new ArrayList<Parse>();
        for (Parse each : p) {
            if (predicate.apply(each)) {
                candidate = each;
            }
            nextLayer.addAll(Arrays.asList(each.getChildren()));
        }
        return deepest(nextLayer, candidate, predicate);
    }


}