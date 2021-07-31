package io.github.tux2603.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class FlagStateTick extends BukkitRunnable {

    private GameState gameState;

    public FlagStateTick(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void run() {

        // TODO: This code is not very DRY. Fix it
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (gameState.getPlayerClass(player).canStealFlag()) {
                // If this is a blue team player and they are within 3 blocks of the red flag, steal it
                if (gameState.getPlayerTeam(player) == PlayerTeam.BLUE && gameState.getRedFlagLocation().distance(player.getLocation()) < 3 && !gameState.isRedFlagStolen()) {
                    gameState.stealRedFlag(player);
                    Bukkit.broadcastMessage(gameState.getPlayerTeam(player).getColor() + player.getDisplayName() + ChatColor.WHITE + " has stolen the red flag!");

                    // If the player's max speed is greater than 0.18, slow down the player
                    if (player.getWalkSpeed() > 0.18) {
                        player.setWalkSpeed(0.18f);
                    }
                }

                // If this is a red team player and they are within 3 blocks of the blue flag, steal it
                if (gameState.getPlayerTeam(player) == PlayerTeam.RED && gameState.getBlueFlagLocation().distance(player.getLocation()) < 3 && !gameState.isBlueFlagStolen()) {
                    gameState.stealBlueFlag(player);
                    Bukkit.broadcastMessage(gameState.getPlayerTeam(player).getColor() + player.getDisplayName() + ChatColor.WHITE + " has stolen the blue flag!");

                    // If the player's max speed is greater than 0.18, slow down the player
                    if (player.getWalkSpeed() > 0.18) {
                        player.setWalkSpeed(0.18f);
                    }
                }
            }

            // If the player has a flag and is within 3 blocks of their own base, capture it
            if (gameState.playerHasBlueFlag(player) && gameState.getRedBaseLocation().distance(player.getLocation()) < 3) {
                gameState.captureBlueFlag(player);
                Bukkit.broadcastMessage(gameState.getPlayerTeam(player).getColor() + player.getDisplayName() + ChatColor.WHITE + " has captured the blue flag!");

                // Show a big pop up message to all players displaying the score
                gameState.showScore(gameState.getPlayerTeam(player).getColor() + player.getDisplayName() + ChatColor.WHITE + " Scored!");

                // Correct the player's speed to the proper value
                player.setWalkSpeed(Loadouts.getSpeed(gameState.getPlayerClass(player)));
            }

            if (gameState.playerHasRedFlag(player) && gameState.getBlueBaseLocation().distance(player.getLocation()) < 3) {
                gameState.captureRedFlag(player);
                Bukkit.broadcastMessage(gameState.getPlayerTeam(player).getColor() + player.getDisplayName() + ChatColor.WHITE + " has captured the red flag!");

                // Show a big pop up message to all players displaying the score
                gameState.showScore(gameState.getPlayerTeam(player).getColor() + player.getDisplayName() + ChatColor.WHITE + " Scored!");

                // Correct the player's speed to the proper value
                player.setWalkSpeed(Loadouts.getSpeed(gameState.getPlayerClass(player)));
            }

            // If the player is within 3 blocks of their own dropped flag, return it
            if (gameState.isRedFlagDropped() && gameState.getPlayerTeam(player) == PlayerTeam.RED && gameState.getRedFlagLocation().distance(player.getLocation()) < 3) {
                gameState.returnRedFlag(player);
                Bukkit.broadcastMessage(gameState.getPlayerTeam(player).getColor() + player.getDisplayName() + ChatColor.WHITE + " has returned the red flag!");
            }

            if (gameState.isBlueFlagDropped() && gameState.getPlayerTeam(player) == PlayerTeam.BLUE && gameState.getBlueFlagLocation().distance(player.getLocation()) < 3) {
                gameState.returnBlueFlag(player);
                Bukkit.broadcastMessage(gameState.getPlayerTeam(player).getColor() + player.getDisplayName() + ChatColor.WHITE + " has returned the blue flag!");
            }
        }
    }
}
