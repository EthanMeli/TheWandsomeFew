package com.ethan.thewandsomefew.server;

public final class TickEngine implements Runnable {
  
  private final long tickMillis;
  private final Runnable onTick;
  private volatile boolean running = true;

  public TickEngine(long tickMillis, Runnable onTick) {
    this.tickMillis = tickMillis;
    this.onTick = onTick;
  }

  public void stop() {
    running = false;
  }

  /**
   * Game Tick System for Server
   * Compute NextTickTime based on currentTimeMillis
   * Update every time tickMillis (600ms)
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
