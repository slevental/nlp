package org.slevental.anaphora.core.txt;

import java.util.*;

import static java.lang.Math.max;

/**
 * Red-Black tree implementation
 *
 * @author Stanislav Levental
 * @version 3/28/13
 */
public class RBTree<E extends Interval> extends AbstractSet<E> implements IntervalCollection<E> {
    /**
     * RB-tree root
     */
    private Entry<E> root = null;

    @Override
    public Iterator<E> iterator() {
        List<E> res = new ArrayList<E>();
        traverse(root, res);
        return res.iterator();
    }

    @Override
    public int size() {
        return size(root);
    }

    @Override
    public boolean add(E e) {
        root = insert(root, e);
        return true;
    }

    @Override
    public Iterable<E> intersects(final E o){
        List<E> aux = new ArrayList<E>();
        intersects(aux, root, o);
        return aux;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null)
            throw new NullPointerException();
        Entry<E> n = root;
        E target = (E) o;
        while (n != null){
            if      (cmp(n, target) > 0)     { n = n.left;  }
            else if (cmp(n, target) < 0)     { n = n.right; }
            else                             { return true; }
        }
        return false;
    }

    private void traverse(Entry<E> node, List<E> aux){
        if (node == null) return;
        traverse(node.left, aux);
        aux.add(node.value);
        traverse(node.right, aux);
    }

    private void intersects(List<E> aux, Entry<E> node, E target) {
        if (node == null)                    { return; }
        if (cmpMax(node.left, target.getLo()))  { intersects(aux, node.left, target); }
        if (node.value.intersects(target))   { aux.add(node.value); }
        if (cmpMin(node.right, target.getHi())) { intersects(aux, node.right, target); }
    }

    private Entry<E> insert(Entry<E> node, E e) {
        if (node == null) return new Entry<E>(e);

        int cmp = cmp(node, e);
        if (cmp >  0) { node.left = insert(node.left, e); }
        if (cmp <  0) { node.right = insert(node.right, e); }
        if (cmp == 0) { node.value = e; }

        // balancing
        if (isRed(node.right) && !isRed(node.left))      { node = rotateLeft(node);  }
        if (isRed(node.left)  &&  isRed(node.left.left)) { node = rotateRight(node); }
        if (isRed(node.left)  &&  isRed(node.right))     { flipColors(node); }

        //update node state
        node.size = size(node.left) + size(node.right) + 1;
        node.max = Math.max(node.max, Math.max(max(node.left), max(node.right)));
        node.min = Math.min(node.min, Math.min(min(node.left), min(node.right)));

        return node;
    }

    private int min(Entry<E> node) {
        return node == null ? Integer.MAX_VALUE : node.min;
    }

    private int max(Entry<E> node) {
        return node == null ? 0 : node.max;
    }

    private boolean isRed(Entry<E> node) {
        return node != null && node.red;
    }

    private boolean cmpMax(Entry<E> node, int lo) {
        return node != null && node.max >= lo;
    }

    private boolean cmpMin(Entry<E> node, int val) {
        return node != null && val >= node.min;
    }

    private int cmp(Entry<E> node, E e) {
        return node.value.compareTo(e);
    }

    private Entry<E> rotateLeft(Entry<E> node){
        Entry<E> r = node.right;
        node.right = r.left;
        r.red      = node.red;
        node.max = Math.max(max(node.right), node.max);
        node.min = Math.min(min(node.right), node.min);
        node.red   = true;
        r.left     = node;
        r.size     = node.size;
        node.size  = size(node.left) + size(node.right) + 1;
        return r;
    }

    private Entry<E> rotateRight(Entry<E> node){
        Entry<E> l = node.left;
        node.left = l.right;
        l.red = node.red;
        node.red = true;
        node.max = Math.max(max(node.left), node.max);
        node.min = Math.min(min(node.left), node.min);
        l.right = node;
        l.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return l;
    }

    private void flipColors(Entry<E> node){
        node.red = true;
        node.left.red = false;
        node.right.red = false;
    }

    private int size( Entry<E> node) {
        return node == null ? 0 : node.size;
    }

    public boolean check(){
        boolean passed = true;
        passed &= isBalanced();
        passed &= checkMax(root, root.max);
        return passed;
    }

    private boolean isBalanced() {
        int black = 0;
        Entry<E> x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.left;
        }
        return isBalanced(root, black);
    }

    private boolean checkMax(Entry<E> node, int max) {
        boolean c = true;
        if (node.left != null) { c &= checkMax(node.left, node.max); }
        if (node.right != null) { c &= checkMax(node.right, node.max); }
        return c && max >= node.max;
    }

    private boolean isBalanced(Entry<E> x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.left, black) && isBalanced(x.right, black);
    }

    private static class Entry<E extends Interval> {
        /**
         * Link to value
         */
        E value;

        /**
         * left/right links
         */
        Entry<E> left,right;

        /**
         * Max interval hi-pos in sub-tree
         */
        int max, min;

        /**
         * Size of underlying tree
         */
        int size = 1;

        /**
         * true - node is red, false - node is black
         */
        boolean red = true;

        Entry(E value) {
            this.value = value;
            this.max = value.getHi();
            this.min = value.getLo();
        }
    }
}
