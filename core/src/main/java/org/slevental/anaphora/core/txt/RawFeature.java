package org.slevental.anaphora.core.txt;

public class RawFeature implements Feature{
    private final String feature;

    public RawFeature(String feature) {
        this.feature = feature;
    }

    @Override
    public String getName() {
        return feature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RawFeature)) return false;

        RawFeature that = (RawFeature) o;

        if (feature != null ? !feature.equals(that.feature) : that.feature != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return feature != null ? feature.hashCode() : 0;
    }

    @Override
    public String toString() {
        return feature;
    }
}
