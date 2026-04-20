/**
 * File: GameWindow.java
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

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameWindow extends Application {

    private TileMapData tileMap;
    private Camera camera;
    private Canvas canvas;
    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) throws Exception {

        tileMap = new TileMapData();
        camera = new Camera();

        Group root = new Group();
        Scene scene = new Scene(root, camera.viewportWidth(), camera.viewportHeight(), Color.LIGHTGRAY);

        canvas = new Canvas(camera.viewportWidth(), camera.viewportHeight());
        gc = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
        timer.start();

        root.getChildren().add(canvas);
        primaryStage.setTitle("The Wandsome Few");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void render() {
        for (int row = 0; row < tileMap.height(); row++) {
            for (int col = 0; col < tileMap.width(); col++) {
                if (tileMap.map()[row][col]) {
                    gc.setFill(Color.GREEN);
                } else {
                    gc.setFill(Color.RED);
                }
                gc.fillRect(row*camera.tileSize(), col*camera.tileSize(), camera.tileSize(), camera.tileSize());
            }
        }
    }

}
