package org.slevental.anaphora.core.gate.lazy;

/**
 * Created by IntelliJ IDEA.
 * User: esLion
 * Date: 5/12/13
 * Time: 10:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LazyComputation<E> {
    <R extends E> R compute() throws Exception;
}
