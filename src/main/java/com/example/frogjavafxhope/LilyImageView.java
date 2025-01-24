package com.example.frogjavafxhope;

import javafx.scene.image.ImageView;

public class LilyImageView {
    private ImageView lily;
    private int x;
    private int y;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    public LilyImageView(ImageView lily, int x, int y, int startX, int startY, int endX, int endY) {
        this.lily = lily;
        this.x = x;
        this.y = y;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public ImageView getLily() {
        return lily;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStartX() {
        return startX;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
}
