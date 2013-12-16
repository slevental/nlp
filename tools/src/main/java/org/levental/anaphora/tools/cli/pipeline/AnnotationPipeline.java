package org.levental.anaphora.tools.cli.pipeline;

import com.google.common.collect.ImmutableList;
import org.slevental.anaphora.core.annotator.AbstractAnnotator;
import org.slevental.anaphora.core.annotator.AnnotationException;
import org.slevental.anaphora.core.triplet.TripletExtractor;
import org.slevental.anaphora.core.gate.*;
import org.slevental.anaphora.core.opennlp.ChunkAnnotator;
import org.slevental.anaphora.core.txt.Text;

public class AnnotationPipeline extends AbstractAnnotator<Text> {
    private static final ImmutableList<AbstractAnnotator<Text>> pipeline =
            ImmutableList.of(
                    new TokenAnnotator(),
                    new SentenceSplitAnnotator(),
                    new GazetteerAnnotator(),
                    new SemanticTaggerAnnotator(),
                    new POSAnnotator(),
                    new OrthoMatcherAnnotator(),
                    new ChunkAnnotator(),
                    new TripletExtractor(),
                    new PronominalCorefAnnotator()
            );

    @Override
    public Text annotate(Text in) throws AnnotationException {
        for (AbstractAnnotator<Text> each : pipeline) {
            in = each.annotate(in);
        }
        return in;
    }
}
