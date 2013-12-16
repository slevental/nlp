package org.slevental.anaphora.core.gate;

public class SemanticTaggerAnnotator extends JapeAnnotator{
    public static final String NER_RULES = "/rules/gate/ner/main.jape";

    public SemanticTaggerAnnotator() {
        super(GATE_TRANSDUCER, SemanticTaggerAnnotator.class.getResource(NER_RULES));
    }
}
