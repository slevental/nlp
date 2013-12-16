package org.slevental.anaphora.core.serial;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertNotNull;

public class HtmlParserTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testParsingResourceFile() throws Exception {
        String parsed = Normalizer.normalizeSpaces(HtmlParser.parseBody(getClass().getResourceAsStream("/bbc.html")));
        assertNotNull(parsed);
        System.out.println(Normalizer.normalizeHtmlEscaping(parsed));
    }
}
