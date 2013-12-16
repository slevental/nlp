package org.slevental.anaphora.core.txt;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * @author Stanislav Levental
 * @version 3/28/13
 */
public class RBTreeTest {
    RBTree<Annotation> tree;

    @Before
    public void setUp() throws Exception {
        tree = new RBTree<Annotation>();
    }

    @After
    public void tearDown() throws Exception {
        assertTrue(tree.check());
    }

    @Test
    public void testFillWith1MValuesAndCheck() throws Exception {
       Random random = new Random(19);
        int N = 1000000;
        for (int i = 0; i < N; i++) {
            tree.add(rndInterval(random, 1000));
        }
        assertTrue(tree.check());
        assertEquals(N, tree.size());

    }

    @Test
    public void testInsertSize() throws Exception {
        tree.add(interval(10, 12));
        tree.add(interval(5, 8));
        tree.add(interval(6, 8));
        tree.add(interval(9, 18));
        tree.add(interval(11, 28));
        assertEquals(5, tree.size());
    }

    @Test
    public void testInsertToSameValue() throws Exception {
        tree.add(interval(0, 0));
        tree.add(interval(0, 0));
        assertEquals(1, tree.size());
    }

    @Test
    public void testIntersection_performanceTest() throws Exception {
        Random random = new Random(19);
        int N = 10000;
        IntervalCollection<Annotation> setImpl = new IntervalSet<Annotation>();
        System.out.println("inserting...");
        for (int i = 0; i < N; i++) {
            Annotation a = rndInterval(random, 1000);
            tree.add(a);
            setImpl.add(a);
        }
        System.out.println("done");
        System.out.println("finding intersections...");
        for (int n = 128; n < 5000; n *= 2) {
            System.out.print("N = " + n);
            Stopwatch treeTime = new Stopwatch();
            Stopwatch setTime = new Stopwatch();
            int size = 0;
            for (int i = 0; i < n; i++) {
                Annotation r = rndInterval(random, 100);
                treeTime.start();
                Set<Annotation> intersectsTree = newHashSet(tree.intersects(r));
                treeTime.stop();

                setTime.start();
                Set<Annotation> intersectsSet = newHashSet(setImpl.intersects(r));
                setTime.stop();
                Sets.SetView<Annotation> diff = Sets.difference(intersectsSet, intersectsTree);
                assertTrue(diff.toString(), diff.isEmpty());
                size += intersectsSet.size();
            }
            System.out.println(" ; Intersections = " + size + " ; Tree time = " + treeTime.elapsedMillis() +
                    " ; Set time = " + setTime.elapsedMillis());
        }
        System.out.println("done");
    }

    private Annotation rndInterval(Random random, int max) {
        int n1 = random.nextInt(max);
        int n2 = random.nextInt(max);
        return interval(Math.min(n1, n2), Math.max(n1, n2));
    }

    private Annotation interval(int lo, int hi) {
        return Annotation.builder().type(AnnotationType.Token).interval(lo, hi).build();
    }
}
