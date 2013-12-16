package org.levental.anaphora.tools.corpora;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.levental.anaphora.tools.jcmd.DirectoryConverter;
import org.levental.anaphora.tools.corpora.ui.Main;

import java.io.File;

public class Launcher {
    public static class Options {
        @Parameter(names = {"--src", "-s"}, required = true, converter = DirectoryConverter.class)
        public File sourceDir;

        @Parameter(names = {"--dst", "-d"}, required = true, converter = DirectoryConverter.class)
        public File destinationDir;
    }

    public static void main(String[] args) {
        Options cfg = new Options();
        JCommander commander = new JCommander(cfg);
        try {
            commander.parse(args);
        } catch (Exception e) {
            commander.usage();
        }
        launch(cfg);
    }

    private static void launch(Options cfg) {
        Main form = Main.init(cfg);
    }
}
