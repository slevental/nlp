package org.levental.yelp.normalizer;

public interface ReviewNormalizer {
    ReviewNormalizer DEFAULT = new CompoundNormalizer();

    String normalize(String review);
}

