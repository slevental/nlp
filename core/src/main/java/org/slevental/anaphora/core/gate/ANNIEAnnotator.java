package org.slevental.anaphora.core.gate;

import gate.Gate;
import gate.creole.ANNIEConstants;

import java.io.File;

public class ANNIEAnnotator extends GateAnnotator {
    static {
        try {
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), ANNIEConstants.PLUGIN_DIR).toURI().toURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ANNIEAnnotator() {
        super("gate.creole.SerialController");
    }
}
