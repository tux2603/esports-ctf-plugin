package io.github.tux2603.manhunt;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class App extends JavaPlugin {
    private UpdateRunnable updateRunnable;
    private Team hunted;
    private Team hunters;

    @Override
    public void onEnable() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        hunted = scoreboard.getTeam("manhuntHunted");
        if (hunted == null)
            hunted = scoreboard.registerNewTeam("manhuntHunted");

        hunters = scoreboard.getTeam("manhuntHunters");
        if (hunters == null)
            hunters = scoreboard.registerNewTeam("manhuntHunters");

        hunted.setDisplayName("Hunted");
        hunted.setColor(ChatColor.BLUE);
        hunted.setPrefix("[HUNTED] ");

        hunters.setDisplayName("Hunters");
        hunters.setColor(ChatColor.RED);
        hunters.setPrefix("[HUNTER] ");

        // Register the commands needed to run the manhunt
        ManhuntCommands commandHandler = new ManhuntCommands(hunted, hunters);
        this.getCommand("start").setExecutor(commandHandler);

        // Trigger an update function every tick
        updateRunnable = new UpdateRunnable(hunted, hunters);
        updateRunnable.runTaskTimer(this, 0, 1);

        getServer().getPluginManager().registerEvents(new CompassDropListener(), this);

        // Plugin initialization done, log it
        getLogger().info("Manhunt plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Remove the teams from the scoreboard
        hunted.unregister();
        hunters.unregister();

        // Stop the update function from triggering
        updateRunnable.cancel();

        // Plugin has been disabled, log it
        getLogger().info("Manhunt plugin disabled");
    }
}
