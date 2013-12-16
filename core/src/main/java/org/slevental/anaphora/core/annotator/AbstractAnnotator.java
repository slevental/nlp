package org.slevental.anaphora.core.annotator;

import com.google.common.base.Function;

import java.io.Closeable;
import java.io.IOException;

public abstract class AbstractAnnotator<E> implements Function<E, E>, Closeable {

    @Override
    public final E apply(E input) {
        try {
            return annotate(input);
        } catch (AnnotationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException {
    }

    public abstract E annotate(E in) throws AnnotationException;
}
