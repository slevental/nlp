package org.levental.yelp.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.levental.yelp.domain.Business;
import org.levental.yelp.domain.Review;
import org.levental.yelp.serial.JsonReaders;
import org.slevental.anaphora.core.annotator.AnnotationException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataReader {

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
        File reviews = new File(file,"");
//        List<Review> reviews = JsonReaders.read(file, Review.class);
//        List<Review> reviews = JsonReaders.read(file, Review.class);
//        System.err.println("Read " + reviews.size() + " reviews");
    }
}
