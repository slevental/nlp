package org.slevental.anaphora.core.opennlp;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.Span;
import org.slevental.anaphora.core.annotator.AbstractAnnotator;
import org.slevental.anaphora.core.txt.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.slevental.anaphora.core.txt.Annotation.builder;
import static org.slevental.anaphora.core.txt.Annotations.getFeature;
import static org.slevental.anaphora.core.txt.StaticFeature.*;
import static org.slevental.anaphora.core.txt.Texts.convert;

public class ChunkAnnotator extends AbstractAnnotator<Text> {

    private ChunkerME tagger;

    public ChunkAnnotator() {
        try {
            InputStream modelIn = getClass().getResourceAsStream("/models/en-chunker.bin");
            ChunkerModel model = new ChunkerModel(modelIn);
            tagger = new ChunkerME(model);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot initialize annotator: " + e.getMessage(), e);
        }
    }

    @Override
    public Text annotate(Text text) {
        for (Annotation sentence : text.getByType(AnnotationType.Sentence)) {
            List<Annotation> intersected = newArrayList(text.getIntersected(sentence, AnnotationType.Token));
            Annotation[] tokens = Annotations.toArray(intersected);
            for (Span each : tagger.chunkAsSpans(getFeature(string, tokens), getFeature(category, tokens))) {
                int startIdx = each.getStart();
                int endIdx = each.getEnd() - 1;
                int lo = tokens[startIdx].getLo();
                int hi = tokens[endIdx].getHi();
                Annotation.Builder b = builder()
                        .type(AnnotationType.Chunk)
                        .interval(lo, hi)
                        .feature(category, each.getType())
                        .feature(string, text.getText(lo, hi));

                // if chunk has only one token, it inherits token's part of speech
                if (startIdx == endIdx)
                    b.feature(category, tokens[startIdx].getFeature(category));

                text.addAnnotation(b.build());
            }
        }
        return text;
    }
}
