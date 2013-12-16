package org.levental.yelp.cli;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slevental.anaphora.core.annotator.AbstractAnnotator;
import org.slevental.anaphora.core.annotator.AnnotationException;
import org.slevental.anaphora.core.triplet.TripletExtractor;
import org.slevental.anaphora.core.gate.*;
import org.slevental.anaphora.core.opennlp.SentenceAnnotator;
import org.slevental.anaphora.core.txt.Text;

@SuppressWarnings({"unchecked"})
public class ConcurrentPipeline {
    private static final Log LOG = LogFactory.getLog(ConcurrentPipeline.class);

    static final AbstractAnnotator[] functions = new AbstractAnnotator[]{
            new TokenAnnotator(),
            new SentenceAnnotator(),
//            new SentenceSplitAnnotator(),
            new GazetteerAnnotator(),
            new SemanticTaggerAnnotator(),
            new POSAnnotator(),
            new OrthoMatcherAnnotator(),
//            new ChunkAnnotator(),
            new PronominalCorefAnnotator(false),
            new JapeAnnotator(JapeAnnotator.GATE_TRANSDUCER, ModelTrainer.class.getResource("/rules/object.jape"))
    };

    public Text execute(Text text) throws AnnotationException {
        for (AbstractAnnotator each : functions) {
            try {
                text = (Text) each.annotate(text);
            } catch (Exception e) {
                LOG.error(String.format("[%s]: Text '%s' failed with message: %s",
                        each.getClass().getCanonicalName(),
                        text.getText(),
                        e.getMessage()
                ));
            }
        }
        return text;
    }
}
