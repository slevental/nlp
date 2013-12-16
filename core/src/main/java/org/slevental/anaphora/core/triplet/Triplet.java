package org.slevental.anaphora.core.triplet;

import opennlp.tools.parser.Parse;

public class Triplet {
    private final Parse subject;
    private final Parse object;
    private final Parse predicate;

    public Triplet(Parse subject, Parse object, Parse predicate) {
        this.subject = subject;
        this.object = object;
        this.predicate = predicate;
    }

    public Parse getSubject() {
        return subject;
    }

    public Parse getObject() {
        return object;
    }

    public Parse getPredicate() {
        return predicate;
    }
}
