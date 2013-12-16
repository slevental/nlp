package org.slevental.anaphora.core.gate;

import gate.*;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialController;
import gate.util.GateException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.slevental.anaphora.core.annotator.AbstractAnnotator;
import org.slevental.anaphora.core.annotator.AnnotationException;
import org.slevental.anaphora.core.txt.Text;
import org.slevental.anaphora.core.txt.Texts;

import java.io.File;
import java.io.IOException;

abstract class GateAnnotator extends AbstractAnnotator<Text> {
    public static final String GATE_HOME = System.getenv("GATE_HOME");
    protected final SerialController controller;

    static {
        try {
            File gateHome = new File(StringUtils.defaultIfBlank(GATE_HOME, SystemUtils.getUserHome() + "/gate"));
            File pluginsHome = new File(gateHome, "plugins");
            Gate.setGateHome(gateHome);
            Gate.setPluginsHome(pluginsHome);
            Gate.setSiteConfigFile(new File(gateHome, "gate.xml"));
            Gate.setUserConfigFile(new File(gateHome, ".gate.xml"));
            Gate.setUserSessionFile(new File(gateHome, ".gate.session"));
            Gate.init();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot initialize gate due to error: " + e.getMessage(), e);
        }
    }

    public GateAnnotator() {
        this("gate.creole.SerialController");
    }

    public GateAnnotator(String controller) {
        try {
            this.controller = (SerialController) Factory.createResource(controller);
        } catch (ResourceInstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addResource(String name, FeatureMap cfg) {
        try {
            controller.add((LanguageAnalyser) Factory.createResource(name, cfg));
        } catch (ResourceInstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Text annotate(Text in) throws AnnotationException {
        Document doc = null;
        try {
            doc = Texts.convert(in);
            push(controller, doc);
            controller.execute();
            pop(controller);
            return Texts.convert(doc);
        } catch (GateException e) {
            throw new AnnotationException("Gate error:" + e.getMessage(), e, getClass());
        } finally {
            try {
                Factory.deleteResource(doc);
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public void close() throws IOException {
        controller.cleanup();
    }

    private static void push(SerialController controller, Document doc) throws GateException {
        setControllersDocument(controller, doc);
    }

    private static void pop(SerialController controller) throws GateException {
        setControllersDocument(controller, null);
    }

    private static void setControllersDocument(SerialController controller, Document doc) throws GateException {
        for (Object each : controller.getPRs()) {
            ((LanguageAnalyser) each).setDocument(doc);
        }
    }
}

