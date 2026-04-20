/**
 * File: Camera.java
 * Module: client
 * Authored By: Ethan Meli
 * Created: 4/19/2026
 * Last Modified: 4/19/2026
 *
 * Purpose:
 *   
 *
 * Responsibilities:
 *   - 
 */
package com.ethan.thewandsomefew.client;

public class Camera {

    private double centerX;
    private double centerY;

    private final double viewportWidth;
    private final double viewportHeight;

    private double tileSize;

    public Camera() {
        centerX = 25;
        centerY = 25;
        tileSize = 16;
        viewportWidth = 400;
        viewportHeight = 400;
    }

    public double[] worldToScreen(double wx, double wy) {
        double screenX = (wx - centerX) * tileSize + viewportWidth / 2;
        double screenY = (wy - centerY) * tileSize + viewportHeight / 2;
        double[] screen = {screenX, screenY};

        return screen;
    }

    public double[] screenToWorld(double sx, double sy) {
        double worldX = (sx - viewportWidth / 2) / tileSize + centerX;
        double worldY = (sy - viewportHeight / 2) / tileSize + centerY;
        double[] world = {worldX, worldY};

        return world;
    }

    public void setCenter(double x, double y) {
        centerX = x;
        centerY = y;
    }

    public void zoom(double delta) {
        // TODO
    }

    public double tileSize() { return tileSize; }
    public double viewportHeight() { return viewportHeight; }
    public double viewportWidth() { return viewportWidth; }

}
