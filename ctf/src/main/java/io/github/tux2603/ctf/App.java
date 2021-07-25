package io.github.tux2603.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class App extends JavaPlugin {
    private GameState gameState;

    @Override
    public void onEnable() {
        gameState = new GameState(this);

        // ##### Register the commands #####
        CTFCommands commandHandler = new CTFCommands(gameState);
        this.getCommand("setBlueBase").setExecutor(commandHandler);
        this.getCommand("setRedBase").setExecutor(commandHandler);
        this.getCommand("startGame").setExecutor(commandHandler);
        this.getCommand("stopGame").setExecutor(commandHandler);
        this.getCommand("restartGame").setExecutor(commandHandler);
        this.getCommand("class").setExecutor(commandHandler);

        Bukkit.broadcastMessage("Capture the flag has been initialized!");
    }

    @Override
    public void onDisable() {
        gameState.unload();
    }
}