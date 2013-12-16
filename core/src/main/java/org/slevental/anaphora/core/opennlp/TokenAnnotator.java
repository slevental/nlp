package org.slevental.anaphora.core.opennlp;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.slevental.anaphora.core.annotator.AbstractAnnotator;
import org.slevental.anaphora.core.txt.Annotation;
import org.slevental.anaphora.core.txt.StaticFeature;
import org.slevental.anaphora.core.txt.Text;

import java.io.IOException;
import java.io.InputStream;

import static org.slevental.anaphora.core.txt.AnnotationType.Token;

public class TokenAnnotator extends AbstractAnnotator<Text> {
    private TokenizerME tokenizer;

    public TokenAnnotator() {
        try {
            InputStream modelIn = getClass().getResourceAsStream("/models/en-token.bin");
            TokenizerModel posModel = new TokenizerModel(modelIn);
            tokenizer = new TokenizerME(posModel);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot initialize annotator: " + e.getMessage(), e);
        }
    }

    @Override
    public Text annotate(Text text) {
        for (Span each : tokenizer.tokenizePos(text.getText())) {
            Annotation a = Annotation
                                     .builder()
                                     .type(Token)
                                     .interval(each.getStart(), each.getEnd())
                                     .feature(StaticFeature.string, text.getText(each.getStart(), each.getEnd()))
                                     .build();
            text.addAnnotation(a);
        }
        return text;
    }
}
