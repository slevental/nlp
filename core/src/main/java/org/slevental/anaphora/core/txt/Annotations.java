package org.slevental.anaphora.core.txt;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import gate.AnnotationSet;
import gate.FeatureMap;
import gate.annotation.AnnotationFactory;
import gate.annotation.AnnotationImpl;
import gate.annotation.DefaultAnnotationFactory;
import gate.util.SimpleFeatureMapImpl;

import java.util.*;

import static org.slevental.anaphora.core.txt.Annotation.builder;

public final class Annotations {
    private static final AnnotationFactory factory = new DefaultAnnotationFactory();

    private Annotations() {
    }

    public static String[] getFeature(StaticFeature f, Annotation... annotations) {
        String[] res = new String[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            res[i] = annotations[i].getFeature(f);
        }
        return res;
    }

    public static String[] getFeature(StaticFeature f, Iterable<Annotation> annotations) {
        List<String> res = Lists.newArrayList();
        for (Annotation each : annotations) {
            res.add(each.<String>getFeature(f));
        }
        return res.toArray(new String[res.size()]);
    }

    public static Annotation[] toArray(List<Annotation> annotations) {
        return annotations.toArray(new Annotation[annotations.size()]);
    }

    public static Collection<Annotation> convert(Text doc, List<gate.Annotation> annotations) {
        List<Annotation> res = Lists.newArrayList();
        for (gate.Annotation each : annotations) {
            res.add(convert(each));
        }
        return res;
    }

    public static Annotation convert(gate.Annotation a) {
        return builder()
                .features(convert(a.getFeatures()))
                .from(a.getStartNode().getOffset().intValue())
                .to(a.getEndNode().getOffset().intValue())
                .type(convert(a.getType()))
                .build();
    }

    private static AnnotationType convert(String gateType) {
        return AnnotationType.valueOf(gateType);
    }

    private static Map<Feature, Object> convert(FeatureMap features) {
        Map<Feature, Object> res = Maps.newHashMap();
        for (Object each : features.keySet()) {
            Feature f = StaticFeature.of(each);
            if (f == null) {
                f = new RawFeature((String) each);
            }
            Object value = features.get(each);

            if (value instanceof gate.Annotation){
                value = convert((gate.Annotation) value);
            }

            res.put(f, value);
        }
        return res;
    }

    public static FeatureMap convert(AnnotationSet dst, Map<Feature, Object> features) {
        FeatureMap res = new SimpleFeatureMapImpl();
        for (Map.Entry<Feature, Object> each : features.entrySet()) {
            Object value = each.getValue();

            if (value instanceof Annotation) {
                Annotation ann = (Annotation) value;
                value = Iterables.getOnlyElement(dst.get(ann.getType().name(), (long) ann.getLo(), (long) ann.getHi()), null);
                if (value == null)
                    throw new IllegalStateException();
            }

            res.put(each.getKey().getName(), value);
        }
        return res;
    }
}
