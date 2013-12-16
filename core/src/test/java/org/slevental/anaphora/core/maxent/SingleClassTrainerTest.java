package org.slevental.anaphora.core.maxent;

import opennlp.model.AbstractModel;
import org.junit.Test;
import org.slevental.anaphora.core.serial.TextProviders;
import org.slevental.anaphora.core.txt.Annotation;
import org.slevental.anaphora.core.txt.AnnotationType;
import org.slevental.anaphora.core.txt.StaticFeature;
import org.slevental.anaphora.core.txt.Text;

import java.net.URI;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: esLion
 * Date: 5/9/13
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class SingleClassTrainerTest {
    @Test
    public void testTrainer() throws Exception {
        Text text = TextProviders.read(new URI("json:data/dest/103C-L055.txt"));

        assertNotNull("Text not found", text);
        SingleClassTrainer trainer = new SingleClassTrainer(text, AnnotationType.Split);
        AbstractModel model = trainer.train();

        Text text2 = TextProviders.read(new URI("json:data/dest/102CTL004.txt"));

        Iterable<Annotation> tokens = text2.getByType(AnnotationType.Token);
        for (Annotation token : tokens) {
            System.out.println(token.getFeature(StaticFeature.string) + "_" + model.getBestOutcome(model.eval(trainer.getContext(token))));
        }


        assertNotNull("Model is null", model);
    }
}
