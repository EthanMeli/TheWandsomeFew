/**
 * File: GameWindow.java
 * Module: client
 * Authored By: Ethan Meli
 * Created: 4/19/2026
 * Last Modified: 4/24/2026
 *
 * Purpose:
 *   Create and render the client application based on server state.
 *   Handle user input and safely transfer packets to and from the server.
 */
package com.ethan.thewandsomefew.client;

import java.io.IOException;

import com.ethan.thewandsomefew.protocol.packets.ClickToWalkPacket;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameWindow extends Application {

    private TileMapData tileMap;
    private Camera camera;
    private Canvas canvas;
    private GraphicsContext gc;
    private ClientState clientState;
    private NetworkThread network;
    private Image localPlayerSprite;
    private Image playerSprite;

    @Override
    public void start(Stage primaryStage) throws Exception {

        clientState = new ClientState();
        network = new NetworkThread(clientState, "127.0.0.1", 43594);
        new Thread(network, "network-thread").start();

        tileMap = new TileMapData();
        camera = new Camera();

        Group root = new Group();
        Scene scene = new Scene(root, camera.viewportWidth(), camera.viewportHeight(), Color.LIGHTGRAY);

        canvas = new Canvas(camera.viewportWidth(), camera.viewportHeight());
        gc = canvas.getGraphicsContext2D();

        canvas.setOnMouseClicked(this::handleClick);

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

        int minX = Math.max(0, (int) Math.floor(topLeft[0]));
        int maxX = Math.min(tileMap.height(), (int) Math.floor(bottomRight[0] + 1));
        int minY = Math.max(0, (int) Math.floor(topLeft[1]));
        int maxY = Math.min(tileMap.width(), (int) Math.floor(bottomRight[1] + 1));

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {

                double[] screen = camera.worldToScreen(x, y);

                if (tileMap.isWalkable(x, y)) {
                    gc.setFill(Color.GREEN);
                } else {
                    gc.setFill(Color.RED);
                }

                gc.fillRect(screen[0], screen[1], camera.tileSize(), camera.tileSize());

                gc.setStroke(Color.BLACK);
                gc.strokeRect(screen[0], screen[1], camera.tileSize(), camera.tileSize());
            }
        }

        // Render NPCs before player so player is on top if on same tile
        for (NpcState npc : clientState.getNpcs()) {
            double[] npcPos = camera.worldToScreen(npc.x(), npc.y());
            gc.setFill(Color.DARKGREEN);
            gc.fillRect(npcPos[0], npcPos[1], camera.tileSize(), camera.tileSize());
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

    /**
     * Handles a mouse click on the canvas by translating the screen coordinates
     * into world tile coordinates and sending a ClickToWalkPacket to the
     * server.
     */
    private void handleClick(MouseEvent event) {

        double sx = event.getSceneX();
        double sy = event.getSceneY();

        double[] screenToWorld = camera.screenToWorld(sx, sy);

        int wx = (int) Math.floor(screenToWorld[0]);
        int wy = (int) Math.floor(screenToWorld[1]);

        try {
            network.sendPacket(new ClickToWalkPacket(wx, wy));
        } catch (IOException e) {
            System.err.println("Failed to send click: " + e.getMessage());
        }
    }
}
