package org.levental.yelp.normalizer;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CompoundNormalizerTest {
    private ReviewNormalizer normalizer = ReviewNormalizer.DEFAULT;

    @Test
    public void replace_U_with_You() throws Exception {
        assertEquals("Hey you", normalizer.normalize("Hey U"));
        assertEquals("you", normalizer.normalize("U"));
        assertEquals("UUU", normalizer.normalize("UUU"));
        assertEquals("U12", normalizer.normalize("U12"));
        assertEquals("you can go there n check the car out.", normalizer.normalize("U can go there n check the car out."));
    }

    @Test
    public void replace_double_qoutes_with_single_one() throws Exception {
        assertEquals(" ' Hello ' ", normalizer.normalize("\"Hello\""));
    }
}
