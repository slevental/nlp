package org.slevental.anaphora.core.txt;

public enum AnnotationType {
    Sentence,
    Token,
    Chunk,
    Subject,
    Predicate,
    Object,

    /**
     * Yelp review stuff
     */
    Property,

    /**
     * GATE annotations
     */
    SpaceToken,
    Person,
    Lookup,
    Organization,
    FirstPerson,
    Location,
    Title,
    JobTitle,
    Split,
    Money,
    Address,
    Date,
    Percent,
    Identifier,
    Temp,
    QuotedText,
    Coreference,
    UrlPre,
    PleonasticIt,

    /**
     * Stanford parser annotations
     */
    SyntaxTreeNode,
    Dependency

}
