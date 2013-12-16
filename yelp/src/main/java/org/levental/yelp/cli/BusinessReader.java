package org.levental.yelp.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.levental.yelp.domain.Business;
import org.levental.yelp.serial.JsonReaders;
import org.slevental.anaphora.core.annotator.AnnotationException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BusinessReader {

    public static class Options {
        @Parameter(names = {"--src", "-s"}, required = true)
        public String source;
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

        read(file);
    }

    private static void read(File file) throws IOException {
        List<Business> businesses = JsonReaders.read(file, Business.class);
        System.err.println("Read " + businesses.size() + " businesses");
    }

}
