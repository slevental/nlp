package org.levental.yelp.domain;

/**
 * Created by IntelliJ IDEA.
 * User: esLion
 * Date: 5/12/13
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
class Votes {
    private int funny;
    private int useful;
    private int cool;

    public int getFunny() {
        return funny;
    }

    public void setFunny(int funny) {
        this.funny = funny;
    }

    public int getUseful() {
        return useful;
    }

    public void setUseful(int useful) {
        this.useful = useful;
    }

    public int getCool() {
        return cool;
    }

    public void setCool(int cool) {
        this.cool = cool;
    }
}
