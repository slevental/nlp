package org.slevental.anaphora.core.centering;

import opennlp.tools.parser.Parse;
import org.slevental.anaphora.core.annotator.AbstractAnnotator;
import org.slevental.anaphora.core.annotator.AnnotationException;
import org.slevental.anaphora.core.opennlp.OpenNLPParser;
import org.slevental.anaphora.core.txt.Annotation;
import org.slevental.anaphora.core.txt.AnnotationType;
import org.slevental.anaphora.core.txt.Text;

/**
 *
 */
public class CenteringAnaphoraAnnotator extends AbstractAnnotator<Text>{

    private final OpenNLPParser parser;

    public CenteringAnaphoraAnnotator() {
        this.parser = new OpenNLPParser();
    }


    @Override
    public Text annotate(Text in) throws AnnotationException {
        for (Annotation sentence : in.getByType(AnnotationType.Sentence)) {
            Parse[] each = parser.parse(in.getText(sentence), 10);
            System.out.println(each);
        }
        return in;
    }
}
