/* $Id$ */
package org.slevental.anaphora.core.gate;

import gate.creole.ResourceInstantiationException;
import gate.creole.Transducer;
import gate.util.GateException;

import java.net.URL;

public class JapeAnnotator extends GateAnnotator {
    public static final String GATE_TRANSDUCER = System.getProperty("gate.transducer.default", "gate.creole.Transducer");
    public static final String MTL_TRANSDUCER = System.getProperty("gate.mtl.tansduser.default", "ca.umontreal.iro.rali.gate.creole.MtlTransducer");

    public JapeAnnotator(URL... grammars) throws GateException {
        this(GATE_TRANSDUCER, grammars);
    }

    public JapeAnnotator(String transducerClassName, URL... grammars) {
        try {
            if (grammars.length == 0) {
                throw new IllegalArgumentException("Empty grammar list for jape trancducer");
            }
            for (URL japeGrammar : grammars) {
                gate.FeatureMap params = gate.Factory.newFeatureMap();
                params.put(Transducer.TRANSD_GRAMMAR_URL_PARAMETER_NAME, japeGrammar);
                controller.add((gate.LanguageAnalyser) gate.Factory.createResource(transducerClassName, params));
            }
        } catch (ResourceInstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public JapeAnnotator() {
    }
}
