package org.slevental.anaphora.core.txt;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Stanislav Levental
 * @version 3/28/13
 */
public class IntervalTest {
    @Test
    public void testIntersects_trueCases() throws Exception {
        assertTrue(interval(50, 100).intersects(interval(0, 51)));
        assertTrue(interval(0, 51).intersects(interval(50, 100)));
        assertTrue(interval(50, 100).intersects(interval(60, 90)));
        assertTrue(interval(60, 90).intersects(interval(0, 100)));
        assertTrue(interval(0, 0).intersects(interval(0, 0)));
        assertTrue(interval(0, 0).intersects(interval(0, 10)));
    }

    @Test
    public void testIntersects_falseCases() throws Exception {
        assertFalse(interval(50, 100).intersects(interval(0, 50)));
        assertFalse(interval(0, 40).intersects(interval(50, 100)));
    }

    private Annotation interval(int lo, int hi) {
        return Annotation.builder().type(AnnotationType.Token).interval(lo, hi).build();
    }
}
