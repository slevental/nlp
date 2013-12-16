package org.slevental.anaphora.core.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.creole.POSTagger;
import gate.creole.Transducer;
import gate.creole.coref.PronominalCoref;
import gate.creole.gazetteer.DefaultGazetteer;
import gate.creole.orthomatcher.OrthoMatcher;
import gate.creole.splitter.SentenceSplitter;
import gate.creole.tokeniser.DefaultTokeniser;
import org.slevental.anaphora.core.annotator.AnnotationException;
import org.slevental.anaphora.core.txt.Annotations;
import org.slevental.anaphora.core.txt.Relations;
import org.slevental.anaphora.core.txt.Text;

import java.util.Map;

public class ANNIEAnnotatorsSequence extends ANNIEAnnotator {

    static {
        try {
            Gate.getCreoleRegister().registerComponent(PronominalCoref.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ANNIEAnnotatorsSequence() {
        FeatureMap cfg;

        /**
         * Tokenizer
         */
        cfg = Factory.newFeatureMap();
        cfg.put(DefaultTokeniser.DEF_TOK_TOKRULES_URL_PARAMETER_NAME, getClass().getResource(TokenAnnotator.RULE));
        addResource(DefaultTokeniser.class.getName(), cfg);

        /**
         * Sentence splitter
         */
        addResource(SentenceSplitter.class.getName(), Factory.newFeatureMap());

        /**
         * Dictionaries
         */
        cfg = Factory.newFeatureMap();
        cfg.put(DefaultGazetteer.DEF_GAZ_LISTS_URL_PARAMETER_NAME, getClass().getResource(GazetteerAnnotator.LISTS));
        addResource(DefaultGazetteer.class.getName(), cfg);

        /**
         * NER rules
         */
        cfg = Factory.newFeatureMap();
        cfg.put(Transducer.TRANSD_GRAMMAR_URL_PARAMETER_NAME, getClass().getResource(SemanticTaggerAnnotator.NER_RULES));
        addResource(SemanticTaggerAnnotator.GATE_TRANSDUCER, cfg);

        /**
         * PartOfSpeech tagging
         */
        addResource(POSTagger.class.getName(), Factory.newFeatureMap());

        /**
         * Relations matching
         */
        addResource(OrthoMatcher.class.getName(), Factory.newFeatureMap());

        /**
         * Anaphora annotator
         */
        addResource(PronominalCoref.class.getName(), Factory.newFeatureMap());
    }

    @Override
    public Text annotate(Text in) throws AnnotationException {
        Text res = super.annotate(in);
        Relations relations = res.getRelations();
        for (Object o : controller.getPRs()) {
            if (o instanceof PronominalCoref){
                Map<gate.Annotation, gate.Annotation> resolvedAnaphora = ((PronominalCoref) o).getResolvedAnaphora();
                for (Map.Entry<gate.Annotation, gate.Annotation> each : resolvedAnaphora.entrySet()) {
                    if (each.getValue() != null)
                        relations.putAnaphora(Annotations.convert(each.getKey()), Annotations.convert(each.getValue()));
                    else
                        System.out.println(each.getKey());
                }
                break;
            }
        }
        return res;
    }
}
