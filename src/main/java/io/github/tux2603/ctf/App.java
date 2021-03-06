package io.github.tux2603.ctf;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    private GameState gameState;

    @Override
    public void onEnable() {
        gameState = new GameState(this);

        // ##### Register the commands #####
        CTFCommands commandHandler = new CTFCommands(gameState);
        this.getCommand("setBlueBase").setExecutor(commandHandler);
        this.getCommand("setRedBase").setExecutor(commandHandler);
        this.getCommand("setRedSpawn").setExecutor(commandHandler);
        this.getCommand("setBlueSpawn").setExecutor(commandHandler);
        this.getCommand("startGame").setExecutor(commandHandler);
        this.getCommand("stopGame").setExecutor(commandHandler);
        this.getCommand("restartGame").setExecutor(commandHandler);
        this.getCommand("class").setExecutor(commandHandler);

        // Players will keep inventory on death
        Bukkit.getWorlds().get(0).setGameRule(GameRule.KEEP_INVENTORY, true);

        Bukkit.broadcastMessage("Capture the flag has been initialized!");
    }

    @Override
    public void onDisable() {
        gameState.unload();
    }
}
