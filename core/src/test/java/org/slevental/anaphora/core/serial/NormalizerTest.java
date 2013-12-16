package org.slevental.anaphora.core.serial;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class NormalizerTest {

    @Test
    public void testSpaceNormalization() throws Exception {
        assertEquals(" ", Normalizer.normalizeSpaces("      "));
        assertEquals("a \n b", Normalizer.normalizeSpaces("a   \n       b"));
    }
}
