package org.slevental.anaphora.core.serial;

import org.htmlcleaner.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class HtmlParser {
    public static final String BODY = "body";
    public static final String PRUNE_TAGS = "script,noscript,object";

    private static final ThreadLocal<HtmlCleaner> cleaner = new ThreadLocal<HtmlCleaner>(){
        @Override
        protected HtmlCleaner initialValue() {
            CleanerProperties properties = new CleanerProperties();
            properties.setPruneTags(PRUNE_TAGS);
            properties.setOmitComments(true);
            properties.setRecognizeUnicodeChars(true);
            return new HtmlCleaner(properties);
        }
    };

    private HtmlParser(){}

    public static Map<String, String> parse(String str){
        return parse(cleaner.get().clean(str));
    }

    public static Map<String, String> parse(File file) throws IOException {
        return parse(cleaner.get().clean(file));
    }

    public static Map<String, String> parse(InputStream in) throws IOException {
        return parse(cleaner.get().clean(in));
    }

    public static String parseBody(InputStream in) throws IOException {
        return String.valueOf(cleaner.get().clean(in).findElementByName(BODY, true).getText());
    }

    private static Map<String, String> parse(TagNode tags) {
        HashMap<String, String> rs = new HashMap<String, String>();
        rs.put(BODY, parseBody(tags.findElementByName(BODY, true)));
        return rs;
    }

    private static String parseBody(TagNode body) {
        final StringBuilder sb = new StringBuilder();
        body.traverse(new TagNodeVisitor() {
            @Override
            public boolean visit(TagNode tagNode, HtmlNode htmlNode) {
                CharSequence txt = tagNode.getText();
                if (txt.length() != 0)
                    sb.append(txt).append(". ");
                return true;
            }
        });
        return sb.toString();
    }
}
