package org.slevental.anaphora.core.maxent;

import com.google.common.collect.Iterables;
import opennlp.model.AbstractModel;
import opennlp.model.Event;
import opennlp.model.EventStream;
import opennlp.model.TrainUtil;
import org.slevental.anaphora.core.txt.Annotation;
import org.slevental.anaphora.core.txt.AnnotationType;
import org.slevental.anaphora.core.txt.StaticFeature;
import org.slevental.anaphora.core.txt.Text;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class SingleClassTrainer implements EventStream {
    private final AnnotationType type;
    private final Iterator<Annotation> tokensIterator;
    private Text text;

    public SingleClassTrainer(Text text, AnnotationType type) {
        this.type = type;
        this.text = text;
        this.tokensIterator = text.getByType(AnnotationType.Token).iterator();
    }

    public AbstractModel train() throws IOException {
        return TrainUtil.train(this, Collections.<String, String>emptyMap(), new HashMap<String, String>());
    }

    @Override
    public Event next() throws IOException {
        Annotation next = tokensIterator.next();
        String clazz = Iterables.isEmpty(text.getIntersected(next, type)) ? "O" : type.name();
        System.out.println(next.getFeature(StaticFeature.string) + "_" + clazz);
        return new Event(clazz, getContext(next));
    }

    public String[] getContext(Annotation next) {
        String txt = next.getFeature(StaticFeature.string);
        return new String[]{
                ".".equals(txt) ? "point" : "not-a-point",
                txt.length() == 1 ? "1-len" : "n-len"
        };
    }

    @Override
    public boolean hasNext() throws IOException {
        return tokensIterator.hasNext();
    }
}
