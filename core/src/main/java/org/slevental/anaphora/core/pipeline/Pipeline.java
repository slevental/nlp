package org.slevental.anaphora.core.pipeline;

import org.slevental.anaphora.core.annotator.AbstractAnnotator;

import java.util.HashMap;
import java.util.Map;

public class Pipeline<E> {
    private final Object config;
    private final Map<AbstractAnnotator<E>, Meta> annotators = new HashMap<AbstractAnnotator<E>, Meta>();

    public Pipeline(Object config) {
        this.config = config;
    }

    private static class Meta {

    }
}
