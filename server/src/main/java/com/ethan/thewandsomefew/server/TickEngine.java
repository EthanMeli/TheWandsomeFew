/**
 * File: TickEngine.java
 * Module: server
 * Authored By: Ethan Meli
 * Created: 3/5/2026
 * Last Modified: 3/8/2026
 * 
 * Purpose:
 *   This file is responsible for defining the tick engine loop
 *   utilized by the server.
 * 
 * Resposibilities:
 *   - Create a consistent tick loop to define timing of information exchange
 *     between clients and the server
 */

package com.ethan.thewandsomefew.server;

/**
 * The TickEngine class defines the timing for information exchange between
 * clients and the server.
 * 
 * <p>Responsibilites:
 * <ul>
 *    <li>Create a consistent tick loop for packet exchange</li>
 * </ul>
 */
public final class TickEngine implements Runnable {
  
  private final long tickMillis;
  private final Runnable onTick;
  private volatile boolean running = true;

  /**
   * Creates a new TickEngine.
   *
   * @param tickMillis represents the amount of time for a single tick (600ms)
   * @param onTick provides a method to be run on individual ticks
   */
  public TickEngine(long tickMillis, Runnable onTick) {
    this.tickMillis = tickMillis;
    this.onTick = onTick;
  }

  public void stop() {
    running = false;
  }

  /**
   * Defines the loop for server ticks
   *
   * @exception InterruptedException occurs when the tick loop is interrupted
   */
  @Override
  public void run() {
    long nextTickTime = System.currentTimeMillis();

    while (running) {

      long now = System.currentTimeMillis();
      if (now >= nextTickTime) {
        onTick.run();
        nextTickTime += tickMillis;
      } else {
          try {
            Thread.sleep(nextTickTime - now);
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
          }
      }
    }
  }
}
