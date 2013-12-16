package org.slevental.anaphora.core.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.creole.gazetteer.DefaultGazetteer;

public class GazetteerAnnotator extends ANNIEAnnotator {
    public static final String LISTS = "/lists/lists.def";

    public GazetteerAnnotator() {
        FeatureMap cfg = Factory.newFeatureMap();
        cfg.put(DefaultGazetteer.DEF_GAZ_LISTS_URL_PARAMETER_NAME, getClass().getResource(LISTS));
        addResource(DefaultGazetteer.class.getName(), cfg);
    }
}
