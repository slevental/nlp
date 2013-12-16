package org.levental.anaphora.tools.corpora.action;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.levental.anaphora.tools.corpora.Launcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;

public class NavigationListener implements ActionListener {
    public static final char CMD_NEXT = 'N';
    public static final char CMD_PREVIOUS = 'P';
    public static final char CMD_SAVE = 'S';

    private final Collection<File> inDir;
    private final File outDir;
    private final NavigationCallback callback;
    private File currentFile;

    public NavigationListener(Launcher.Options cfg, NavigationCallback callback) {
        this.callback = callback;
        this.inDir = FileUtils.listFiles(cfg.sourceDir, FileFileFilter.FILE, FalseFileFilter.FALSE);
        this.outDir = cfg.destinationDir;
    }

    public void next(){
        if (nextAnnotation()){

        }
    }

    private boolean nextAnnotation() {
        return false;
    }

    public void previous(){

    }

    public void save(){

    }

    private File nextFile(){
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd != null && cmd.length() == 1){
            switch (cmd.charAt(0)){
                case CMD_NEXT:
                case CMD_PREVIOUS:
                case CMD_SAVE:
                default:
                    throw new RuntimeException("Illegal command: " + cmd);
            }
        }
    }
}
