package org.slevental.anaphora.core.serial;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.regex.Pattern;

public final class Normalizer {
    private Normalizer(){}

    private static Pattern newLineNormalizer = Pattern.compile("\\s*[\n]+\\s*");

    public static String normalizeSpaces(String str){
        return newLineNormalizer.matcher(str).replaceAll("\n");
    }

    public static String normalizeHtmlEscaping(String str){
        return StringEscapeUtils.unescapeXml(str);
    }
}
