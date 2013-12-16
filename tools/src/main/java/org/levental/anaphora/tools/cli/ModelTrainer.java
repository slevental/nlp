package org.levental.anaphora.tools.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import opennlp.maxent.GIS;
import org.levental.anaphora.tools.jcmd.DirectoryConverter;

import java.io.File;

public class ModelTrainer {
    public static class Options {
        @Parameter(names = {"--src", "-s"}, required = true, converter = DirectoryConverter.class)
        public File sourceDir;
    }

    public static void main(String[] args) {
        Options cfg = new Options();
        JCommander jCommander = new JCommander(cfg);
        try {
            jCommander.parse(args);
        } catch (Exception e) {
            jCommander.usage();
            System.exit(2);
        }

        train(cfg);
    }

    private static void train(Options cfg) {

    }
}
