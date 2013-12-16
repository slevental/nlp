package org.slevental.anaphora.core.gate;

import gate.Factory;
import gate.creole.orthomatcher.OrthoMatcher;

public class OrthoMatcherAnnotator extends ANNIEAnnotator {
    public OrthoMatcherAnnotator() {
        addResource(OrthoMatcher.class.getName(), Factory.newFeatureMap());
    }
}
