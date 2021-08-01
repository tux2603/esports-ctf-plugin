package io.github.tux2603.ctf;

import org.bukkit.scheduler.BukkitRunnable;

public class BlueFlagReturnRunnable extends BukkitRunnable {
    private GameState gameState;

    public BlueFlagReturnRunnable(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void run() {
        gameState.returnBlueFlag();
    }    
}
