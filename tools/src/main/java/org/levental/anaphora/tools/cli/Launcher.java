package org.levental.anaphora.tools.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.tools.ant.types.resources.FileProvider;
import org.levental.anaphora.tools.cli.pipeline.AnnotationPipeline;
import org.levental.anaphora.tools.jcmd.AnnotationTypeConverter;
import org.levental.anaphora.tools.jcmd.DirectoryConverter;
import org.slevental.anaphora.core.annotator.AnnotationException;
import org.slevental.anaphora.core.serial.TextProviders;
import org.slevental.anaphora.core.txt.Annotation;
import org.slevental.anaphora.core.txt.AnnotationType;
import org.slevental.anaphora.core.txt.StaticFeature;
import org.slevental.anaphora.core.txt.Text;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class Launcher {
    public static class Options {
        @Parameter(names = {"--src", "-s"}, required = true, converter = DirectoryConverter.class)
        public File sourceDir;

        @Parameter(names = {"--dst", "-d"}, required = true, converter = DirectoryConverter.class)
        public File destDir;

        @Parameter(names = {"--scheme", "-S"}, required = false)
        public String scheme = "json";
    }


    public static void main(String[] args) throws Exception {
        Options cfg = new Options();
        JCommander commander = new JCommander(cfg);
        try {
            commander.parse(args);
        } catch (Exception e) {
            commander.usage();
            System.exit(5);
        }
        launch(cfg);
    }

    private static void launch(Options cfg) throws Exception{
        AnnotationPipeline pipeline = new AnnotationPipeline();
        for (File each : FileUtils.listFiles(cfg.sourceDir, TrueFileFilter.TRUE, FalseFileFilter.FALSE)) {
            Text txt = new Text(each.getName(), FileUtils.readFileToString(each));
            txt = pipeline.annotate(txt);
            File dst = new File(cfg.destDir, txt.getName());
            dst.createNewFile();
            TextProviders.write(txt, new URI(cfg.scheme + ":" + dst.getAbsolutePath()));
        }
    }
}
