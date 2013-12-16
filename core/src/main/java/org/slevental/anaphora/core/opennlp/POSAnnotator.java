package org.slevental.anaphora.core.opennlp;

import com.google.common.collect.Iterables;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.slevental.anaphora.core.annotator.AbstractAnnotator;
import org.slevental.anaphora.core.txt.*;

import java.io.IOException;
import java.io.InputStream;

public class POSAnnotator extends AbstractAnnotator<Text> {
    private POSTaggerME tagger;

    public POSAnnotator() {
        try {
            InputStream modelIn = getClass().getResourceAsStream("/models/en-pos-maxent.bin");
            POSModel posModel = new POSModel(modelIn);
            tagger = new POSTaggerME(posModel);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot initialize annotator: " + e.getMessage(), e);
        }
    }

    @Override
    public Text annotate(Text text) {
        for (Annotation sentence : text.getByType(AnnotationType.Sentence)) {
            Annotation[] tokens = Iterables.toArray(text.getIntersected(sentence, AnnotationType.Token), Annotation.class);
            String[] tokensTxt = Annotations.getFeature(StaticFeature.string, tokens);
            String[] tags = tagger.tag(tokensTxt);
            for (int i = 0; i < tags.length; i++) {
                tokens[i].addFeature(StaticFeature.category, tags[i]);
            }
        }
        return text;
    }

}
