package org.levental.yelp.normalizer;

import java.util.regex.Pattern;

class CompoundNormalizer implements ReviewNormalizer{
    private static final ReviewNormalizer[] normalizers = new ReviewNormalizer[]{
            replaceAll("(?<!\\w)U(?!\\w)","you"),
            replaceAll("\""," ' "),
    };

    @Override
    public String normalize(String review) {
        for (ReviewNormalizer each : normalizers) {
            review = each.normalize(review);
        }
        return review;
    }

    private static ReviewNormalizer replaceAll(final String pattern, final String str){
        return new ReviewNormalizer() {
            final Pattern p = Pattern.compile(pattern);

            @Override
            public String normalize(String review) {
                return p.matcher(review).replaceAll(str);
            }
        };
    }
}
