package org.slevental.anaphora.core.txt;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import org.apache.commons.lang.Validate;

import javax.annotation.Nullable;
import java.util.*;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author Stanislav Levental
 * @version 3/28/13
 */
public class Text {
    public static final String DEFAULT = "default";
    private final String name;
    private final String text;
    private final Map<String, IntervalCollection<Annotation>> annotations = newHashMap();
    private final Relations relations = new Relations();
    private final Map<AnnotationType, List<Annotation>> typeIndex = new EnumMap<AnnotationType, List<Annotation>>(AnnotationType.class);

    public Text(String name, String text) {
        Validate.notEmpty(name, "Name should be filled for this text");
        Validate.notEmpty(text, "Text should has content");

        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public Relations getRelations(){
        return relations;
    }

    public Iterable<Annotation> getByType(AnnotationType type) {
        return typeIndex.containsKey(type) ? typeIndex.get(type) : ImmutableList.<Annotation>of();
    }

    public Iterable<Annotation> getAllDefault(){
        return annotations.get(DEFAULT);
    }

    public Map<String, IntervalCollection<Annotation>> getAll() {
        return Collections.unmodifiableMap(annotations);
    }

    public Text addAll(Collection<Annotation> annotations) {
        return addAll(DEFAULT, annotations);
    }

    public Text addAll(String name, Collection<Annotation> annotations) {
        for (Annotation each : annotations) {
            addAnnotation(each, name);
        }
        return this;
    }

    public Iterable<Annotation> getIntersected(String name, Annotation a, final AnnotationType... res) {
        final Set<AnnotationType> types = EnumSet.copyOf(Arrays.asList(res));
        return Iterables.filter(getCollection(name).intersects(a), new Predicate<Annotation>() {
            @Override
            public boolean apply(Annotation annotation) {
                return types.contains(annotation.getType());
            }
        });
    }

    public Iterable<Annotation> getIntersected(Annotation a, final AnnotationType... res) {
        return getIntersected(DEFAULT, a, res);
    }

    public String getText(int lo, int hi) {
        return text.substring(lo, hi);
    }

    public String getText(Interval interval) {
        return interval == null ? null : getText(interval.getLo(), interval.getHi());
    }

    public Iterable<String> getText(Iterable<? extends Interval> intervals){
        return Iterables.transform(intervals, new Function<Interval, String>() {
            @Override
            public String apply(Interval input) {
                return getText(input);
            }
        });
    }

    public String getLeftContext(Interval interval, int length){
        return text.substring(Math.max(0, interval.getLo() - length), interval.getLo());
    }

    public String getRightContext(Interval interval, int length){
        return text.substring(interval.getHi(), Math.min(text.length(), interval.getHi() + length));
    }

    public String getTextInContext(Interval i, int len){
        return String.format("[%s] %s [%s]", getLeftContext(i, len), getText(i), getRightContext(i, len));
    }

    public void addAnnotation(Annotation annotation) {
        addAnnotation(annotation, DEFAULT);
    }

    public void addAnnotation(Annotation annotation, String subSet) {
        getCollection(subSet).add(annotation);

        List<Annotation> idx = typeIndex.get(annotation.getType());
        if (idx == null) {
            idx = new ArrayList<Annotation>();
            typeIndex.put(annotation.getType(), idx);
        }
        idx.add(annotation);
    }

    private IntervalCollection<Annotation> getCollection(String name) {
        IntervalCollection<Annotation> res = annotations.get(name);
        if (res == null) {
            res = new RBTree<Annotation>();
            annotations.put(name, res);
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Text)) return false;

        Text text1 = (Text) o;

        if (annotations != null ? !annotations.equals(text1.annotations) : text1.annotations != null) return false;
        if (name != null ? !name.equals(text1.name) : text1.name != null) return false;
        if (relations != null ? !relations.equals(text1.relations) : text1.relations != null) return false;
        if (text != null ? !text.equals(text1.text) : text1.text != null) return false;
        if (typeIndex != null ? !typeIndex.equals(text1.typeIndex) : text1.typeIndex != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (annotations != null ? annotations.hashCode() : 0);
        result = 31 * result + (relations != null ? relations.hashCode() : 0);
        result = 31 * result + (typeIndex != null ? typeIndex.hashCode() : 0);
        return result;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private String text;
        private String name;
        private String annotationSetName;
        private Multimap<String, Annotation> annotations = HashMultimap.create();


        public Builder setAnnotationSetName(String name){
            annotationSetName = name;
            return this;
        }

        public Builder addAnnotation(Annotation annotation){
            annotations.put(annotationSetName, annotation);
            return this;
        }

        public Builder addAnnotation(Annotation... annotations){
            this.annotations.putAll(annotationSetName, Arrays.asList(annotations));
            return this;
        }

        public Builder addAnnotations(Collection<Annotation> annotations){
            this.annotations.putAll(annotationSetName, annotations);
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Text build(){
            Text txt = new Text(name, text);
            for (Map.Entry<String, Collection<Annotation>> each : annotations.asMap().entrySet()) {
                if (each.getKey() == null){
                    txt.addAll(each.getValue());
                }else{
                    txt.addAll(each.getKey(), each.getValue());
                }
            }
            return txt;
        }
    }

}
