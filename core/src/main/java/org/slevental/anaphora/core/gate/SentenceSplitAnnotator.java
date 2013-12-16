package org.slevental.anaphora.core.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.creole.splitter.SentenceSplitter;
import gate.creole.tokeniser.DefaultTokeniser;

public class SentenceSplitAnnotator extends ANNIEAnnotator{
    public SentenceSplitAnnotator() {
        addResource(SentenceSplitter.class.getName(), Factory.newFeatureMap());
    }
}
