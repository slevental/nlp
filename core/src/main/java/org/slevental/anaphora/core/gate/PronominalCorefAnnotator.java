package org.slevental.anaphora.core.gate;

import com.google.common.collect.Iterables;
import gate.Factory;
import gate.Gate;
import org.slevental.anaphora.core.annotator.AnnotationException;
import org.slevental.anaphora.core.txt.*;

import java.util.Map;

@SuppressWarnings({"unchecked"})
public class PronominalCorefAnnotator extends GateAnnotator {
    public static final int DEBUG_CONTEXT_SIZE = 25;
    private boolean debugMode = false;

    static {
        try {
            Gate.getCreoleRegister().registerComponent(PronominalCoref.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PronominalCorefAnnotator(boolean debugMode) {
        this.debugMode = debugMode;
        addResource(PronominalCoref.class.getName(), Factory.newFeatureMap());
    }

    public PronominalCorefAnnotator() {
        this(false);
    }

    @Override
    public Text annotate(Text in) throws AnnotationException {
        Text res = super.annotate(in);
        for (Object o : controller.getPRs()) {
            if (o instanceof PronominalCoref) {
                Map<gate.Annotation, gate.Annotation> resolvedAnaphora = ((PronominalCoref) o).getResolvedAnaphora();
                for (Map.Entry<gate.Annotation, gate.Annotation> each : resolvedAnaphora.entrySet()) {
                    if (each.getValue() != null) {
                        Annotation pronoun = Annotations.convert(each.getKey());
                        Annotation object = Annotations.convert(each.getValue());
                        Annotation token = Iterables.getOnlyElement(res.getIntersected(pronoun, AnnotationType.Token));
                        token.addFeature(StaticFeature.relation, object);

                        if (debugMode) {
                            System.err.println(String.format("%s -> %s",
                                    in.getTextInContext(token, DEBUG_CONTEXT_SIZE),
                                    in.getTextInContext(object, DEBUG_CONTEXT_SIZE)
                            ));
                        }
                    }
                }
                break;
            }
        }
        return res;
    }

}
