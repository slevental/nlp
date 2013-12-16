package org.levental.anaphora.tools.corpora.action;

import java.io.File;

public interface NavigationCallback {
    void fileOpened(File name);
    void fileSaved(File name);
}
