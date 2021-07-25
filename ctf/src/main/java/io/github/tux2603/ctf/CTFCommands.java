package io.github.tux2603.ctf;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        // Parse the first three agruments as a location and save it to the game state
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
            // Get the name of the class that the player is trying to join
            String className = args[0];
            gameState.joinClass((Player)sender, className);
            return true;
        }

        return false;
    }

}
