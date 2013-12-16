package org.slevental.anaphora.core.maxent;

import opennlp.maxent.ContextGenerator;

/**
 * Created by IntelliJ IDEA.
 * User: esLion
 * Date: 5/9/13
 * Time: 12:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class FooContextGenerator implements ContextGenerator{
    @Override
    public String[] getContext(Object o) {
        return new String[]{(String) o};
    }
}
