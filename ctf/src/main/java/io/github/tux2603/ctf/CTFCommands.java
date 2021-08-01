package io.github.tux2603.ctf;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class CTFCommands implements CommandExecutor {
    private GameState gameState;

    public CTFCommands(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch (cmd.getName().toLowerCase()) {
        case "setredbase":
            return setRedBaseLocation(sender, args);
        case "setbluebase":
            return setBlueBaseLocation(sender, args);
        case "setredspawn":
            return setRedSpawnLocation(sender, args);
        case "setbluespawn":
            return setBlueSpawnLocation(sender, args);
        case "startgame":
            return start(sender);
        case "stopgame":
            return stop(sender);
        case "restartgame":
            return restart(sender);
        case "class":
            return joinClass(sender, args);
        default:
            sender.sendMessage("Unknown command");
            return false;
        }
    }

    private boolean setRedBaseLocation(CommandSender sender, String[] args) {
        // Parse the first three arguments as a location and save it to the game state
        if (args.length >= 3) {
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);

            gameState.setRedBaseLocation(x, y, z);
            sender.sendMessage("Red base location set to " + x + ", " + y + ", " + z);
            return true;
        }

        // If no location was specified, use the players's location
        if (args.length == 0 && sender instanceof Player) {
            Location location = ((Player)sender).getLocation();
            gameState.setRedBaseLocation(location);
            sender.sendMessage("Red base location set to " + location.getX() + ", " + location.getY() + ", " + location.getZ());
            return true;
        }


        return false;
    }

    private boolean setBlueBaseLocation(CommandSender sender, String[] args) {
        // Parse the first three arguments as a location and save it to the game state
        if (args.length >= 3) {
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);

            gameState.setBlueBaseLocation(x, y, z);
            sender.sendMessage("Blue base location set to " + x + ", " + y + ", " + z);
            return true;
        }

        // If no location was specified, use the players's location
        if (args.length == 0 && sender instanceof Player) {
            Location location = ((Player)sender).getLocation();
            gameState.setBlueBaseLocation(location);
            sender.sendMessage("Blue base location set to " + location.getX() + ", " + location.getY() + ", " + location.getZ());
            return true;
        }

        return false;
    }

    private boolean setRedSpawnLocation(CommandSender sender, String[] args) {
        // Parse the first three arguments as a location and save it to the game state
        if (args.length >= 3) {
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);

            gameState.setRedSpawnLocation(x, y, z);
            sender.sendMessage("Red spawn location set to " + x + ", " + y + ", " + z);
            return true;
        }

        // If no location was specified, use the players's location
        if (args.length == 0 && sender instanceof Player) {
            Location location = ((Player)sender).getLocation();
            gameState.setRedSpawnLocation(location);
            sender.sendMessage("Red spawn location set to " + location.getX() + ", " + location.getY() + ", " + location.getZ());
            return true;
        }

        return false;
    }

    private boolean setBlueSpawnLocation(CommandSender sender, String[] args) {
        // Parse the first three arguments as a location and save it to the game state
        if (args.length >= 3) {
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);

            gameState.setBlueSpawnLocation(x, y, z);
            sender.sendMessage("Blue spawn location set to " + x + ", " + y + ", " + z);
            return true;
        }

        // If no location was specified, use the players's location
        if (args.length == 0 && sender instanceof Player) {
            Location location = ((Player)sender).getLocation();
            gameState.setBlueSpawnLocation(location);
            sender.sendMessage("Blue spawn location set to " + location.getX() + ", " + location.getY() + ", " + location.getZ());
            return true;
        }

        return false;
    }

    private boolean start(CommandSender sender) {
        // Send a message to whoever is calling the command
        sender.sendMessage("Starting game");

        // Start the game
        gameState.start();
        return gameState.isRunning();
    }

    private boolean stop(CommandSender sender) {
        // Send a message to whoever is calling the command
        sender.sendMessage("Stopping game");

        // Stop the game
        gameState.stop();
        return !gameState.isRunning();
    }

    private boolean restart(CommandSender sender) {
        // Send a message to whoever is calling the command
        sender.sendMessage("Restarting game");
        gameState.restart();
        return gameState.isRunning();
    }

    private boolean joinClass(CommandSender sender, String[] args) {
        // Make sure that there is at least one argument and that it is a player
        if (args.length > 0 && sender instanceof Player) {
            Player player = (Player)sender;
            // Get the name of the class that the player is trying to join
            PlayerClass playerClass = PlayerClass.getByName(args[0]);

            // Check that the game is running
            if (!gameState.isRunning()) {
                player.sendMessage(ChatColor.RED + "The game must be running to join a class!");
            }

            // Check that the player is on a team
            if (gameState.getPlayerTeam(player) == PlayerTeam.NONE) {
                player.sendMessage(ChatColor.RED + "You must be on a team to join a class!");
            }

            // Check that the player is not already in a class
            if (gameState.getPlayerClass(player) != PlayerClass.NONE) {
                player.sendMessage(ChatColor.RED + "You are already in a class!");
            }

            gameState.setPlayerClass(player, playerClass);

            player.getInventory().clear();
            player.getInventory().setContents(Loadouts.getInventory(playerClass));
            player.getInventory().setArmorContents(Loadouts.getArmor(playerClass));

            // Set the chest plate color
            LeatherArmorMeta chestplateMeta = (LeatherArmorMeta)player.getInventory().getChestplate().getItemMeta();

            if (gameState.getPlayerTeam(player) == PlayerTeam.RED) {
                chestplateMeta.setColor(Color.RED);
            }

            else if (gameState.getPlayerTeam(player) == PlayerTeam.BLUE) {
                chestplateMeta.setColor(Color.BLUE);
            }

            player.getInventory().getChestplate().setItemMeta(chestplateMeta);

            // Teleport the player to the base
            if (gameState.getPlayerTeam(player) == PlayerTeam.RED) {
                player.teleport(gameState.getRedBaseLocation());
            }

            else if (gameState.getPlayerTeam(player) == PlayerTeam.BLUE) {
                player.teleport(gameState.getBlueBaseLocation());
            }

            player.setWalkSpeed(Loadouts.getSpeed(playerClass));
            player.addPotionEffects(Loadouts.getPotionEffects(playerClass));

            // Send a message to the player
            player.sendMessage(ChatColor.GREEN + "You have joined the " + playerClass.getName() + " class!");

            return true;
        }

        return false;
    }
}
