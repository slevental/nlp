package org.slevental.anaphora.core.annotator;

public class AnnotationException extends Exception {
    public AnnotationException(String message, Throwable cause, Class annotator) {
        super(annotator.getCanonicalName() + " throws " + message, cause);
    }
}
