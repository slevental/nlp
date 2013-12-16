package org.slevental.anaphora.core.centering;

import org.slevental.anaphora.core.annotator.AbstractAnnotator;
import org.slevental.anaphora.core.annotator.AnnotationException;
import org.slevental.anaphora.core.txt.*;

public class NounVerbNounExtractor extends AbstractAnnotator<Text> {
    public static final String COMMA = ",";

    @Override
    public Text annotate(Text in) throws AnnotationException {
        for (Annotation sentence : in.getByType(AnnotationType.Sentence)) {
            String txt = in.getText(sentence);
            Annotation subject = null;
            Annotation predicate = null;
            Annotation object = null;
            for (Annotation token : in.getIntersected(sentence, AnnotationType.Chunk)) {
                String pos = token.getFeature(StaticFeature.category);
                if ("NP".equals(pos)){
                    if (subject == null)
                        subject = token;
                    else if (predicate != null){
                        object = token;
                        break;
                    }
                }else if ("VP".equals(pos) && predicate == null){
                    predicate = token;
                }
            }
            System.out.println(String.format("%s => %s => %s", in.getText(subject), in.getText(predicate), in.getText(object)));
        }
        return in;
    }
}
