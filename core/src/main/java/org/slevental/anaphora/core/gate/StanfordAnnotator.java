package org.slevental.anaphora.core.gate;

import gate.Gate;

import java.io.File;

import static gate.Factory.newFeatureMap;

public class StanfordAnnotator extends GateAnnotator {
    static {
        try {
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Parser_Stanford").toURI().toURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StanfordAnnotator() {
        addResource("gate.stanford.Parser", newFeatureMap());
    }
}
