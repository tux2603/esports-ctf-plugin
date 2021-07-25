package io.github.tux2603.ctf;

import org.bukkit.scheduler.BukkitRunnable;

public class GameTickRunnable extends BukkitRunnable {

    private GameState gameState;

    public GameTickRunnable(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void run() {
        gameState.tick();
    }    
}
