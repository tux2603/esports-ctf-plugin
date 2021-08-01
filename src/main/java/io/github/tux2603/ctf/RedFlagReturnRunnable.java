package io.github.tux2603.ctf;

import org.bukkit.scheduler.BukkitRunnable;

public class RedFlagReturnRunnable extends BukkitRunnable {

    private GameState gameState;

    public RedFlagReturnRunnable(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void run() {
        gameState.returnRedFlag();
    }    
}
