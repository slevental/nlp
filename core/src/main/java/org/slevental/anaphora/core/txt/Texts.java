package org.slevental.anaphora.core.txt;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import gate.AnnotationSet;
import gate.Document;
import gate.util.GateException;
import gate.util.InvalidOffsetException;
import opennlp.tools.util.Span;

import java.util.*;

public final class Texts {
    private static final Ordering<gate.Annotation> offsetOrder = Ordering.from(new Comparator<gate.Annotation>() {
        @Override
        public int compare(gate.Annotation o1, gate.Annotation o2) {
            return ComparisonChain.start()
                    .compare(o1.getStartNode().getOffset().intValue(), o2.getStartNode().getOffset().intValue())
                    .compare(o1.getEndNode().getOffset().intValue(), o2.getEndNode().getOffset().intValue())
                    .compare(o1.getType(), o2.getType())
                    .compare(o1.getId(), o2.getId())
                    .result();
        }
    });

    private Texts() {
    }

    public static Text convert(Document gateDoc) {
        Text annText = new Text(gateDoc.getName(), gateDoc.getContent().toString());
        annText.addAll(Annotations.convert(annText, offsetOrder.sortedCopy(gateDoc.getAnnotations())));
        Set<String> setNames = gateDoc.getAnnotationSetNames();
        if (setNames != null) {
            for (String setName : setNames) {
                annText.addAll(setName, Annotations.convert(annText, offsetOrder.sortedCopy(gateDoc.getAnnotations(setName))));
            }
        }
        return annText;
    }

    public static Interval convert(Span span) {
        return new Interval(span.getStart(), span.getEnd());
    }

    public static Document convert(Text txt) throws GateException {
        Document doc = gate.Factory.newDocument(txt.getText());
        doc.setName(txt.getName());
        for (Map.Entry<String, IntervalCollection<Annotation>> each : txt.getAll().entrySet()) {
            if (Text.DEFAULT.equalsIgnoreCase(each.getKey())) {
                addAll(doc.getAnnotations(), each.getValue());
                continue;
            }
            addAll(doc.getAnnotations(each.getKey()), each.getValue());
        }
        return doc;
    }

    private static void addAll(AnnotationSet dst, Collection<Annotation> src) throws InvalidOffsetException {
        for (Annotation each : src) {
            dst.add((long) each.getLo(), (long) each.getHi(), each.getType().name(), Annotations.convert(dst, each.getFeatures()));
        }
    }

}
