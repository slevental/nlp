package org.slevental.nlp.neural;

import org.junit.Test;

import static java.util.Arrays.deepEquals;
import static junit.framework.Assert.assertTrue;

public class RBMTest {
    @Test
    public void testFillingOfRows() throws Exception {
        double[][] arr = {{1, 2, 3}, {4, 5, 6}};
        RBM.fillRows(arr, 0, 9);
        assertTrue(deepEquals(arr, new double[][]{{9, 9, 9}, {4, 5, 6}}));
        RBM.fillRows(arr, 1, 8);
        assertTrue(deepEquals(arr, new double[][]{{9, 9, 9}, {8, 8, 8}}));
    }

    @Test
    public void testFillingOfColumns() throws Exception {
        double[][] arr = {{1, 2, 3}, {4, 5, 6}};
        RBM.fillColumns(arr, 0, 9);
        assertTrue(deepEquals(arr, new double[][]{{9, 2, 3}, {9, 5, 6}}));
        RBM.fillColumns(arr, 1, 9);
        assertTrue(deepEquals(arr, new double[][]{{9, 9, 3}, {9, 9, 6}}));
    }
}
