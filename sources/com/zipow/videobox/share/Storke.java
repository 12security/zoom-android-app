package com.zipow.videobox.share;

import java.util.List;

public class Storke {
    private int color;
    private List<Integer> points;
    private int width;

    Storke(int i, int i2, List<Integer> list) {
        this.color = i;
        this.width = i2;
        this.points = list;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public List<Integer> getPoints() {
        return this.points;
    }

    public void setPoints(List<Integer> list) {
        this.points = list;
    }
}
