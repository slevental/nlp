package org.slevental.anaphora.core.serial;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slevental.anaphora.core.txt.Annotation;
import org.slevental.anaphora.core.txt.AnnotationType;
import org.slevental.anaphora.core.txt.StaticFeature;
import org.slevental.anaphora.core.txt.Text;

import java.io.File;
import java.net.URI;

import static junit.framework.Assert.assertEquals;
import static org.slevental.anaphora.core.serial.TextProviders.read;
import static org.slevental.anaphora.core.serial.TextProviders.write;

public class TextProvidersTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private File tmpTarget;

    @Before
    public void setUp() throws Exception {
        tmpTarget = tmp.newFile("test.json");
    }

    @Test
    public void testSerialization() throws Exception {
        Text expected = new Text("name", "text");
        expected.addAnnotation(Annotation.builder().interval(1, 2).feature(StaticFeature.creationRule, "qweqw")
                .feature(StaticFeature.kind, 234)
                .type(AnnotationType.Address).build());
        Class.forName(JsonProvider.class.getName());
        URI uri = new URI("json:" + tmpTarget.getAbsolutePath());
        write(expected, uri);
        Text actual = read(uri);

        assertEquals(expected, actual);
    }
}
