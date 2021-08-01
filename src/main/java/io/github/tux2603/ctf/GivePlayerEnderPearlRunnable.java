package io.github.tux2603.ctf;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GivePlayerEnderPearlRunnable extends BukkitRunnable {
    private GameState gameState;
    private Player player;

    public GivePlayerEnderPearlRunnable(GameState gameState, Player player) {
        this.gameState = gameState;
        this.player = player;
    }

    @Override
    public void run() {
        if (gameState.isRunning()) {
            // Make sure that the player doesn't already have an ender pearl
            if (player.getInventory().contains(Material.ENDER_PEARL)) {
                return;
            }

            // Make sure that the player's class is either normal or scout
            if (gameState.getPlayerClass(player) == PlayerClass.NORMAL || gameState.getPlayerClass(player) == PlayerClass.SCOUT) {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
            }
        }
    }
}
