package org.slevental.nlp.neural;

import com.google.common.collect.Table;

/**
 * Created by IntelliJ IDEA.
 * User: esLion
 * Date: 6/2/13
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Trainer {
    Model train(Table table);
}
