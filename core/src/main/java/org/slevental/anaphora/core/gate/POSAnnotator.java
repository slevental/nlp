package org.slevental.anaphora.core.gate;

import gate.Factory;

public class POSAnnotator extends ANNIEAnnotator{
    public POSAnnotator() {
        addResource(gate.creole.POSTagger.class.getName(), Factory.newFeatureMap());
    }
}
