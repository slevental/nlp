package org.slevental.anaphora.core.txt;

import com.google.common.collect.Maps;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.CompareToBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Stanislav Levental
 * @version 3/28/13
 */
public class Annotation extends Interval {
    private final int id ;
    private final AnnotationType type;
    private final Map<Feature, Object> features;

    private Annotation(int id, AnnotationType type, int lo, int hi, Map<Feature, Object> features) {
        super(lo, hi);
        Validate.notNull(type, "Annotation type cannot be null");
        this.type = type;
        this.id = id;
        this.features = features;
    }

    public Annotation addFeature(StaticFeature f, Object value) {
        features.put(f, value);
        return this;
    }

    @SuppressWarnings({"unchecked"})
    public <E> E getFeature(StaticFeature f) {
        E res = (E) features.get(f);
        return res == null ? (E) f.getDefault() : res;
    }

    public int getId() {
        return id;
    }

    public <E> E getFeature(StaticFeature f, Class<E> clazz) {
        Object res = features.get(f);
        return clazz.cast(res == null ? f.getDefault() : res);
    }

    public Map<Feature, Object> getFeatures() {
        return Collections.unmodifiableMap(features);
    }

    public AnnotationType getType() {
        return type;
    }

    @Override
    public int compareTo(Interval i) {
        Annotation that = (Annotation) i;
        return new CompareToBuilder()
                .appendSuper(super.compareTo(that))
                .append(this.type, that.type)
                .toComparison()
                ;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append("[").append(lo).append(", ").append(hi).append(']');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation)) return false;
        if (!super.equals(o)) return false;

        Annotation that = (Annotation) o;

        if (type != null ? type != that.type : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private static final AtomicInteger idGenerator = new AtomicInteger();

        private AnnotationType type;
        private Map<Feature, Object> features = Maps.newHashMap();
        private int lo, hi, id;

        public Builder fromAnnotation(Annotation a) {
            type = a.type;
            lo = a.lo;
            hi = a.hi;
            features.putAll(a.features);
            return this;
        }

        public Builder from(int lo) {
            this.lo = lo;
            return this;
        }

        public Builder interval(Interval i) {
            this.lo = i.lo;
            this.hi = i.hi;
            return this;
        }

        public Builder to(int hi) {
            this.hi = hi;
            return this;
        }

        public Builder id(int id){
            this.id = id;
            return this;
        }

        public Builder interval(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
            return this;
        }

        public Builder feature(Feature name, Object value) {
            features.put(name, value);
            return this;
        }

        public Builder features(Map<Feature, Object> m) {
            features.putAll(m);
            return this;
        }

        public Builder type(AnnotationType type) {
            this.type = type;
            return this;
        }

        public Annotation build() {
            return new Annotation(id > 0 ? id : idGenerator.incrementAndGet(), type, lo, hi, features);
        }
    }
}
