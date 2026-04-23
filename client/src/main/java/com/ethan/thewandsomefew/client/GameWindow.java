/**
 * File: GameWindow.java
 * Module: client
 * Authored By: Ethan Meli
 * Created: 4/19/2026
 * Last Modified: 4/22/2026
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
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameWindow extends Application {

    private TileMapData tileMap;
    private Camera camera;
    private Canvas canvas;
    private GraphicsContext gc;
    private ClientState clientState;
    private Image localPlayerSprite;
    private Image playerSprite;

    @Override
    public void start(Stage primaryStage) throws Exception {

        clientState = new ClientState();
        NetworkThread network = new NetworkThread(clientState, "127.0.0.1", 43594);
        new Thread(network, "network-thread").start();

        tileMap = new TileMapData();
        camera = new Camera();

        Group root = new Group();
        Scene scene = new Scene(root, camera.viewportWidth(), camera.viewportHeight(), Color.LIGHTGRAY);

        canvas = new Canvas(camera.viewportWidth(), camera.viewportHeight());
        gc = canvas.getGraphicsContext2D();

        localPlayerSprite = new Image(getClass().getResourceAsStream("/sprites/knight/knight01.png"));
        playerSprite = new Image(getClass().getResourceAsStream("/sprites/characters/standard01.png"));

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

        gc.clearRect(0, 0, camera.viewportWidth(), camera.viewportHeight());

        PlayerState localPlayer = clientState.getLocalPlayer();
        if (localPlayer == null) {
            // TODO: Create Loading Message
            return;
        }

        camera.setCenter(localPlayer.x(), localPlayer.y());

        double[] topLeft = camera.screenToWorld(0, 0);
        double[] bottomRight = camera.screenToWorld(camera.viewportWidth(), camera.viewportHeight());

        int minRow = Math.max(0, (int) topLeft[0]);
        int maxRow = Math.min(tileMap.height(), (int) bottomRight[0] + 1);
        int minCol = Math.max(0, (int) topLeft[1]);
        int maxCol = Math.min(tileMap.width(), (int) bottomRight[1] + 1);

        for (int row = minRow; row < maxRow; row++) {
            for (int col = minCol; col < maxCol; col++) {

                double[] screen = camera.worldToScreen(row, col);

                if (tileMap.isWalkable(row, col)) {
                    gc.setFill(Color.GREEN);
                } else {
                    gc.setFill(Color.RED);
                }

                gc.fillRect(screen[0], screen[1], camera.tileSize(), camera.tileSize());
            }
        }

        for (PlayerState player : clientState.getPlayers()) {
            double[] playerPos = camera.worldToScreen(player.x(), player.y());
            if (player.id() != localPlayer.id()) {
                // gc.setFill(Color.ORANGE);
                // gc.fillRect(playerPos[0], playerPos[1], camera.tileSize(), camera.tileSize());
                gc.drawImage(playerSprite, playerPos[0], playerPos[1], camera.tileSize(), camera.tileSize());
            } 
        }

        double[] localPlayerPos = camera.worldToScreen(localPlayer.x(), localPlayer.y());
        // gc.setFill(Color.PURPLE);
        // gc.fillRect(localPlayerPos[0], localPlayerPos[1], camera.tileSize(), camera.tileSize());
        gc.drawImage(localPlayerSprite, localPlayerPos[0], localPlayerPos[1], camera.tileSize(), camera.tileSize());
    }
}
