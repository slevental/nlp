package org.slevental.anaphora.core.gate.lazy;

import gate.*;
import gate.event.AnnotationSetListener;
import gate.event.GateListener;
import gate.util.InvalidOffsetException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class LazyAnnotationSet implements AnnotationSet {
    private final LazyComputation<AnnotationSet> computation;
    private AnnotationSet delegate;

    public LazyAnnotationSet(LazyComputation<AnnotationSet> computation) {
        this.computation = computation;
    }

    public void compute() {
        if (delegate == null) {
            try {
                delegate = computation.compute();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public void add(Integer id, Long start, Long end, String type, FeatureMap features) throws InvalidOffsetException {
        compute();
        delegate.add(id, start, end, type, features);
    }

    @Override
    public AnnotationSet get(String type, FeatureMap constraints) {
        compute();
        return delegate.get(type, constraints);
    }

    @Override
    public AnnotationSet get(String type, Set featureNames) {
        compute();
        return delegate.get(type, featureNames);
    }

    @Override
    public AnnotationSet get(String type, FeatureMap constraints, Long offset) {
        compute();
        return delegate.get(type, constraints, offset);
    }

    @Override
    public AnnotationSet get(Long offset) {
        compute();
        return delegate.get(offset);
    }

    @Override
    public AnnotationSet get(Long startOffset, Long endOffset) {
        compute();
        return delegate.get(startOffset, endOffset);
    }

    @Override
    public AnnotationSet get(String type, Long startOffset, Long endOffset) {
        compute();
        return delegate.get(type, startOffset, endOffset);
    }

    @Override
    public AnnotationSet getCovering(String neededType, Long startOffset, Long endOffset) {
        compute();
        return delegate.getCovering(neededType, startOffset, endOffset);
    }

    @Override
    public AnnotationSet getContained(Long startOffset, Long endOffset) {
        compute();
        return delegate.getContained(startOffset, endOffset);
    }

    @Override
    public Node firstNode() {
        compute();
        return delegate.firstNode();
    }

    @Override
    public Node lastNode() {
        compute();
        return delegate.lastNode();
    }

    @Override
    public Node nextNode(Node node) {
        compute();
        return delegate.nextNode(node);
    }

    @Override
    public void addAnnotationSetListener(AnnotationSetListener l) {
        compute();
        delegate.addAnnotationSetListener(l);
    }

    @Override
    public void removeAnnotationSetListener(AnnotationSetListener l) {
        compute();
        delegate.removeAnnotationSetListener(l);
    }

    @Override
    public void addGateListener(GateListener l) {
        compute();
        delegate.addGateListener(l);
    }

    @Override
    public void removeGateListener(GateListener l) {
        compute();
        delegate.removeGateListener(l);
    }

    @Override
    public Integer add(Node start, Node end, String type, FeatureMap features) {
        compute();
        return delegate.add(start, end, type, features);
    }

    @Override
    public Integer add(Long start, Long end, String type, FeatureMap features) throws InvalidOffsetException {
        compute();
        return delegate.add(start, end, type, features);
    }

    @Override
    public boolean add(Annotation a) {
        compute();
        return delegate.add(a);
    }

    @Override
    public Iterator<Annotation> iterator() {
        compute();
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        compute();
        return delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        compute();
        return delegate.toArray(a);
    }

    @Override
    public int size() {
        compute();
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        compute();
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        compute();
        return delegate.contains(o);
    }

    @Override
    public boolean remove(Object o) {
        compute();
        return delegate.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        compute();
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Annotation> c) {
        compute();
        return delegate.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        compute();
        return delegate.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        compute();
        return delegate.removeAll(c);
    }

    @Override
    public void clear() {
        compute();
        delegate.clear();
    }

    @Override
    public Annotation get(Integer id) {
        compute();
        return delegate.get(id);
    }

    @Override
    public AnnotationSet get() {
        compute();
        return delegate.get();
    }

    @Override
    public AnnotationSet get(String type) {
        compute();
        return delegate.get(type);
    }

    @Override
    public AnnotationSet get(Set<String> types) {
        compute();
        return delegate.get(types);
    }

    @Override
    public String getName() {
        compute();
        return delegate.getName();
    }

    @Override
    public Set<String> getAllTypes() {
        compute();
        return delegate.getAllTypes();
    }

    @Override
    public Document getDocument() {
        compute();
        return delegate.getDocument();
    }
}
