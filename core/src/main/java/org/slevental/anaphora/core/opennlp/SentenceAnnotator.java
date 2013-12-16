package org.slevental.anaphora.core.opennlp;


import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;
import org.slevental.anaphora.core.annotator.AbstractAnnotator;
import org.slevental.anaphora.core.txt.Annotation;
import org.slevental.anaphora.core.txt.Interval;
import org.slevental.anaphora.core.txt.Text;

import java.io.IOException;
import java.io.InputStream;

import static org.slevental.anaphora.core.txt.AnnotationType.Sentence;
import static org.slevental.anaphora.core.txt.StaticFeature.string;
import static org.slevental.anaphora.core.txt.Texts.convert;

public class SentenceAnnotator extends AbstractAnnotator<Text> {
    private final SentenceDetectorME detector;

    public SentenceAnnotator() {
        try {
            InputStream modelIn = getClass().getResourceAsStream("/models/en-sent.bin");
            SentenceModel model = new SentenceModel(modelIn);
            detector = new SentenceDetectorME(model);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create annotator due to error: " + e.getMessage(), e);
        }
    }

    @Override
    public Text annotate(Text text) {
        for (Span sentence : detector.sentPosDetect(text.getText())) {
            Interval interval = convert(sentence);
            Annotation a = Annotation
                    .builder()
                    .type(Sentence)
                    .interval(interval)
                    .feature(string, text.getText(interval))
                    .build();
            text.addAnnotation(a);
        }
        return text;
    }
}
