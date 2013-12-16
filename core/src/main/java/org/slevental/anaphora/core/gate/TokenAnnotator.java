package org.slevental.anaphora.core.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.creole.tokeniser.DefaultTokeniser;

public class TokenAnnotator extends ANNIEAnnotator {
    public static final String RULE = System.getProperty("tokeniser.rule", "/rules/gate/tokenizer/CustomTokeniser.rules");

    public TokenAnnotator() {
        FeatureMap cfg = Factory.newFeatureMap();
        cfg.put(DefaultTokeniser.DEF_TOK_TOKRULES_URL_PARAMETER_NAME, getClass().getResource(RULE));
        addResource(DefaultTokeniser.class.getName(), cfg);
    }
}
