package org.slevental.anaphora.core.txt;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author Stanislav Levental
 * @version 3/28/13
 */
public class Interval implements Comparable<Interval> {
    protected final int lo;
    protected final int hi;

    public Interval(int lo, int hi) {
        if (lo > hi)
            throw new IllegalArgumentException("lo > hi (lo=" + lo + ", hi=" + hi + ")");
        this.lo = lo;
        this.hi = hi;
    }

    public int getLo() {
        return lo;
    }

    public int getHi() {
        return hi;
    }

    public int getSize(){
        return hi - lo;
    }

    public boolean intersects(Interval o) {
        return contains(o.lo) || contains(o.hi) || o.contains(this);
    }

    public boolean contains(int pos, boolean strict) {
        return (strict && pos < hi && pos > lo) || (!strict && pos <= hi && pos >= lo);
    }

    public boolean contains(int pos) {
        return contains(pos, true);
    }

    public boolean contains(Interval o) {
        return contains(o.lo, false) && contains(o.hi, false);
    }

    public int compareTo(Interval that) {
        return new CompareToBuilder()
                .append(this.lo, that.lo)
                .append(this.hi, that.hi)
                .toComparison();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interval interval = (Interval) o;

        if (hi != interval.hi) return false;
        if (lo != interval.lo) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lo;
        result = 31 * result + hi;
        return result;
    }
}
