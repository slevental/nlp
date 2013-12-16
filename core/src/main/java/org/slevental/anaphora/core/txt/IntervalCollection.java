package org.slevental.anaphora.core.txt;

import java.util.Collection;

public interface IntervalCollection<E extends Interval> extends Collection<E>{
    Iterable<E> intersects(E o);
}
