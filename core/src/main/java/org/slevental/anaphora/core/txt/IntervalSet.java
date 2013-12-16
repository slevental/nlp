package org.slevental.anaphora.core.txt;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.HashSet;

public class IntervalSet<E extends Interval> extends HashSet<E> implements IntervalCollection<E> {
    public Iterable<E> intersects(final E o) {
        return Collections2.filter(this, new Predicate<E>() {
            public boolean apply(E e) {
                return e.intersects(o);
            }
        });
    }
}
