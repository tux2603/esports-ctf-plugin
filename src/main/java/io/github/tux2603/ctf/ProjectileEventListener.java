package io.github.tux2603.ctf;

import org.bukkit.Bukkit;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectileEventListener implements Listener {
    GameState gameState;

    public ProjectileEventListener(GameState gameState) {
        this.gameState = gameState;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Bukkit.broadcastMessage("Detected a projectile launch event!");
        
        if (event.getEntity() instanceof EnderPearl) {
            EnderPearl enderPearl = (EnderPearl)event.getEntity();
            if (enderPearl.getShooter() instanceof Player) {
                Player player = (Player)enderPearl.getShooter();

                if (gameState.getPlayerClass(player) == PlayerClass.NORMAL) {
                    // Create an event to give the player a new ender pearl after 200 ticks
                    GivePlayerEnderPearlRunnable runnable = new GivePlayerEnderPearlRunnable(gameState, player);
                    runnable.runTaskLater(gameState.getPlugin(), 200);
                }

                else if (gameState.getPlayerClass(player) == PlayerClass.SCOUT) {
                    // Create an event to give the player a new ender pearl after 300 ticks
                    GivePlayerEnderPearlRunnable runnable = new GivePlayerEnderPearlRunnable(gameState, player);
                    runnable.runTaskLater(gameState.getPlugin(), 300);
                }
            }
        }
    }
}
