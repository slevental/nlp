package org.levental.yelp.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import opennlp.tools.cmdline.PerformanceMonitor;
import org.apache.commons.lang.StringUtils;
import org.levental.yelp.normalizer.ReviewNormalizer;
import org.slevental.anaphora.core.annotator.AnnotationException;
import org.slevental.anaphora.core.txt.Annotation;
import org.slevental.anaphora.core.txt.AnnotationType;
import org.slevental.anaphora.core.txt.StaticFeature;
import org.slevental.anaphora.core.txt.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ModelTrainer {

    public static final String UNKNOWN = "unknown";

    public static class Options {
        @Parameter(names = {"--src", "-s"}, required = true)
        public String source;

        @Parameter(names = {"--limit", "-l"})
        public int limit = Integer.MAX_VALUE;
    }

    public static void main(String[] args) throws IOException, AnnotationException {
        Options cfg = new Options();
        JCommander jCommander = new JCommander(cfg);
        try {
            jCommander.parse(args);
        } catch (Exception e) {
            jCommander.usage();
            System.exit(2);
        }

        File file = new File(cfg.source);
        if (!file.exists()) {
            System.err.println("Not found: " + file.getAbsolutePath());
            System.exit(3);
        }

        parse(file, cfg.limit);
    }

    private static void parse(File in, int limit) throws IOException, AnnotationException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new BufferedInputStream(new FileInputStream(in)));
        List<Text> toParse = new ArrayList<Text>();
        read(limit, parser, toParse);

        System.err.println("Read " + toParse.size() + " reviews");

        ConcurrentPipeline p = new ConcurrentPipeline();
        PerformanceMonitor mon = new PerformanceMonitor(System.err, "reviews");
        mon.startAndPrintThroughput();
        for (ListIterator<Text> iterator = toParse.listIterator(); iterator.hasNext(); ) {
            Text text = iterator.next();
            iterator.set(p.execute(text));
            mon.incrementCounter();
        }
        mon.stopAndPrintFinalResult();

        Multimap<String, String> res = HashMultimap.create();

        for (Text text : toParse) {
            for (Annotation obj : text.getByType(AnnotationType.Property)) {
                res.put(text.getText(obj), (String) obj.getFeature(StaticFeature.adjective));
            }
        }

        for (Map.Entry<String, Collection<String>> each : res.asMap().entrySet()) {
            System.out.println(each.getKey() + " -> " + each.getValue());
        }
    }


    private static void read(int limit, JsonParser parser, List<Text> toParse) throws IOException {
        JsonToken token;
        String fieldName;
        while ((token = parser.nextToken()) != null) {
            fieldName = parser.getCurrentName();
            switch (token) {
                case VALUE_STRING:
                    if ("text".equals(fieldName)) {
                        String text = parser.getText();
                        if (StringUtils.isBlank(text)) {
                            continue;
                        }
                        toParse.add(new Text(UNKNOWN, ReviewNormalizer.DEFAULT.normalize(text)));
                        if (toParse.size() >= limit)
                            return;
                    }
                    break;
                default:
            }
        }
    }
}
