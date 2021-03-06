package io.github.tux2603.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private GameState gameState;

    public PlayerDeathListener(GameState gameState) {
        this.gameState = gameState;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // TODO: if the player has a flag set up a timer to return the flag after some amount of time
        if(gameState.playerHasBlueFlag(event.getEntity())) {
            gameState.dropBlueFlag(event.getEntity());
            Bukkit.broadcastMessage(gameState.getPlayerTeam(event.getEntity()).getColor() + event.getEntity().getName() + ChatColor.WHITE + " has dropped the blue flag!");

            // Start a timer to return the flag after 600 ticks
            BlueFlagReturnRunnable blueFlagReturnRunnable = new BlueFlagReturnRunnable(gameState);
            blueFlagReturnRunnable.runTaskLater(gameState.getPlugin(), 600);
        }

        if(gameState.playerHasRedFlag(event.getEntity())) {
            gameState.dropRedFlag(event.getEntity());
            Bukkit.broadcastMessage(gameState.getPlayerTeam(event.getEntity()).getColor() + event.getEntity().getName() + ChatColor.WHITE + " has dropped the red flag!");

            // Start a timer to return the flag after 600 ticks
            RedFlagReturnRunnable redFlagReturnRunnable = new RedFlagReturnRunnable(gameState);
            redFlagReturnRunnable.runTaskLater(gameState.getPlugin(), 600);
        }

        gameState.setPlayerClass(event.getEntity(), PlayerClass.DEAD);

        event.setKeepInventory(true);
    }
    
}
