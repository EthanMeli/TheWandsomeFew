/**
 * File: Player.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/8/2026
 * Last Modified: 4/27/2026
 *
 * Purpose:
 *   This file is responsible for defining the logic for Players.
 */
package com.ethan.thewandsomefew.server;

/**
 * The Player class handles all logic related to Players (users).
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Extends LivingEntity class</li>
 * </ul>
 *
 * <p>
 * Notes:
 * <ul>
 * <li>Currently no added logic from LivingEntity class</li>
 * </ul>
 */
public final class Player extends LivingEntity {

    private static final int PLAYER_MAX_HP = 10;
    private static final int PLAYER_MAX_HIT = 1;
    private static final int PLAYER_ATTACK_SPEED = 4;

    public Player(int id, int x, int y) {
        super(id, x, y, PLAYER_MAX_HP, PLAYER_MAX_HIT, PLAYER_ATTACK_SPEED);
    }
}
