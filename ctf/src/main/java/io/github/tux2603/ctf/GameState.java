package io.github.tux2603.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

// TODO: This file is a bit long, maybe split it up?

public class GameState {


    private Location redBaseLocation;
    private Location blueBaseLocation;
    private Location redFlagLocation;
    private Location blueFlagLocation;

    private Scoreboard scoreboard;

    private Team blueTeam;
    private Team redTeam;

    private Objective flagsCaptured;
    private Objective flagsRecovered;
    private Objective playerClasses;

    private FlagStateTick flagTick;

    private JavaPlugin plugin;

    private boolean running = false;
    private boolean isRedBaseLocationSet = false;
    private boolean isBlueBaseLocationSet = false;

    private Player redFlagHolder = null;
    private Player blueFlagHolder = null;

    private boolean isRedFlagStolen = false;
    private boolean isBlueFlagStolen = false;
    private boolean isRedFlagAtBase = true;
    private boolean isBlueFlagAtBase = true;


    /**
     * Creates an object to track the state of a capture the flag game
     * @param plugin The plugin that is running the game
     */
    public GameState(JavaPlugin plugin) {
        this.plugin = plugin;
        this.running = false;

        // Set all locations to (0, 0, 0) in the plugins main world
        redBaseLocation = new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0);
        blueBaseLocation = new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0);
        redFlagLocation = new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0);
        blueFlagLocation = new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0);

        // ##### Set up the teams #####
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Register the teams only if they don't already exist
        redTeam = scoreboard.getTeam("ctfRed");
        if (redTeam == null) {
            redTeam = scoreboard.registerNewTeam("ctfRed");
        }

        blueTeam = scoreboard.getTeam("ctfBlue");
        if (blueTeam == null) {
            blueTeam = scoreboard.registerNewTeam("ctfBlue");
        }

        // Set the team prefixes
        redTeam.setPrefix("[RED] ");
        blueTeam.setPrefix("[BLUE] ");

        // Set the team display names
        redTeam.setDisplayName("Red Team");
        blueTeam.setDisplayName("Blue Team");

        // Set the team colors
        redTeam.setColor(ChatColor.RED);
        blueTeam.setColor(ChatColor.BLUE);

        // ##### Set up the objectives #####

        // Register the objectives only if they don't already exist
        flagsCaptured = scoreboard.getObjective("flagsCaptured");
        if (flagsCaptured == null) {
            flagsCaptured = scoreboard.registerNewObjective("flagsCaptured", "dummy", "Flags Captured");
        }

        flagsRecovered = scoreboard.getObjective("flagsRecovered");
        if (flagsRecovered == null) {
            flagsRecovered = scoreboard.registerNewObjective("flagsRecovered", "dummy", "Flags Recovered");
        }

        playerClasses = scoreboard.getObjective("playerHasClass");
        if (playerClasses == null) {
            playerClasses = scoreboard.registerNewObjective("playerHasClass", "dummy", "Player Class is Set");
        }

        // ##### Set up the listeners #####

        plugin.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), plugin);
    }


    /**
     * Called when the plugin needs to be unloaded.
     * Forces the game to stop and then unregisters the teams and objectives.
     */
    public void unload() {
        stop();

        // Unregister the teams
        redTeam.unregister();
        blueTeam.unregister();

        // Unregister the objectives
        flagsCaptured.unregister();
        flagsRecovered.unregister();
        playerClasses.unregister();
    }

    /**
     * Starts the game.
     */
    public void start() {
        // Do nothing if the game is already running
        if (running) {
            return;
        }

        // Can't start if the bases aren't set
        if (!isRedBaseLocationSet || !isBlueBaseLocationSet) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[CTF] " + ChatColor.RED + "The bases must be set before the game can be started.");
            return;
        }

        // Put the flags at the base
        redFlagLocation = redBaseLocation.clone();
        redFlagLocation.setY(redBaseLocation.getY() + 1);

        blueFlagLocation = blueBaseLocation.clone();
        blueFlagLocation.setY(blueBaseLocation.getY() + 1);

        // Put a block of colored wool at the base
        redBaseLocation.getBlock().setType(Material.RED_WOOL);
        blueBaseLocation.getBlock().setType(Material.BLUE_WOOL);

        // Put a colored banner at the flag locations
        redFlagLocation.getBlock().setType(Material.RED_BANNER);
        blueFlagLocation.getBlock().setType(Material.BLUE_BANNER);

        // Set the scores for all online players to 0
        for (Player player : Bukkit.getOnlinePlayers()) {
            flagsCaptured.getScore(player.getName()).setScore(0);
            flagsRecovered.getScore(player.getName()).setScore(0);
            playerClasses.getScore(player.getName()).setScore(PlayerClass.NONE.getId());
        }

        // Set the team scores for flags captured and flags recovered
        for (PlayerTeam team : PlayerTeam.values()) {
            flagsCaptured.getScore(team.getName() + "Team").setScore(0);
            flagsRecovered.getScore(team.getName() + "Team").setScore(0);
        }

        redFlagHolder = null;
        blueFlagHolder = null;

        // Set the state booleans
        isRedFlagStolen = false;
        isBlueFlagStolen = false;
        isRedFlagAtBase = true;
        isBlueFlagAtBase = true;

        // Start the tick events
        flagTick = new FlagStateTick(this);
        flagTick.runTaskTimer(plugin, 0, 1);

        // Mark the game as running
        running = true;

        Bukkit.broadcastMessage(ChatColor.GOLD + "[CTF] " + ChatColor.GREEN + "The game has started!");
    }


    /**
     * Stops the game.
     */
    public void stop() {
        // Do nothing if the game is not running
        if (!running) {
            return;
        }

        // TODO: Stop the game

        // Mark the game as stopped
        running = false;

        Bukkit.broadcastMessage(ChatColor.GOLD + "[CTF] " + ChatColor.RED + "The game has ended!");
    }


    /**
     * Restarts the game.
     */
    public void restart() {
        // Do nothing if the game is not running
        if (!running) {
            return;
        }

        stop();
        start();
    }

    public void stealRedFlag(Player player) {
        redFlagLocation.getBlock().setType(Material.AIR);
        player.getInventory().setHelmet(new ItemStack(Material.RED_BANNER));

        isRedFlagStolen = true;
        isRedFlagAtBase = false;

        redFlagHolder = player;
    }


    public void stealBlueFlag(Player player) {
        blueFlagLocation.getBlock().setType(Material.AIR);
        player.getInventory().setHelmet(new ItemStack(Material.BLUE_BANNER));

        isBlueFlagStolen = true;
        isBlueFlagAtBase = false;

        blueFlagHolder = player;
    }


    public void dropRedFlag(Player player) {
        // Only drop the flag if the player is the holder
        if (playerHasRedFlag(player)) {
            redFlagLocation = player.getLocation();
            redFlagLocation.getBlock().setType(Material.RED_BANNER);
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            isRedFlagStolen = false;
            redFlagHolder = null;
        }
    }


    public void dropBlueFlag(Player player) {
        // Only drop the flag if the player is the holder
        if (playerHasBlueFlag(player)) {
            blueFlagLocation = player.getLocation();
            blueFlagLocation.getBlock().setType(Material.BLUE_BANNER);
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            isBlueFlagStolen = false;
            blueFlagHolder = null;
        }
    }


    public void returnRedFlag() {
        // If the flag isn't captured, it's sitting somewhere. Set that block to air
        if (!isRedFlagStolen) {
            redFlagLocation.getBlock().setType(Material.AIR);
        }

        // If the flag is on someone's head, remove it
        if (redFlagHolder != null) {
            redFlagHolder.getInventory().setHelmet(new ItemStack(Material.AIR));
        }

        // Set the flag to the base
        redFlagLocation = redBaseLocation.clone();
        redFlagLocation.setY(redFlagLocation.getY() + 1);
        redFlagLocation.getBlock().setType(Material.RED_BANNER);

        isRedFlagAtBase = true;
        isRedFlagStolen = false;
        redFlagHolder = null;
    }


    public void returnRedFlag(Player player) {
        returnRedFlag();
        Score playeScore = flagsRecovered.getScore(player.getName());
        Score teamScore = flagsRecovered.getScore(getPlayerTeam(player).getName() + " team");
        playeScore.setScore(playeScore.getScore() + 1);
        teamScore.setScore(teamScore.getScore() + 1);
    }


    public void returnBlueFlag() {
        // If the flag isn't captured, it's sitting somewhere. Set that block to air
        if (!isBlueFlagStolen) {
            blueFlagLocation.getBlock().setType(Material.AIR);
        }

        // If the flag is on someone's head, remove it
        if (blueFlagHolder != null) {
            blueFlagHolder.getInventory().setHelmet(new ItemStack(Material.AIR));
        }

        // Set the flag to the base
        blueFlagLocation = blueBaseLocation.clone();
        blueFlagLocation.setY(blueFlagLocation.getY() + 1);
        blueFlagLocation.getBlock().setType(Material.BLUE_BANNER);

        isBlueFlagAtBase = true;
        isBlueFlagStolen = false;
        blueFlagHolder = null;
    }


    public void returnBlueFlag(Player player) {
        returnBlueFlag();

        // Increase the scores
        Score playeScore = flagsRecovered.getScore(player.getName());
        Score teamScore = flagsRecovered.getScore(getPlayerTeam(player).getName() + " team");
        playeScore.setScore(playeScore.getScore() + 1);
        teamScore.setScore(teamScore.getScore() + 1);
    }


    public void captureRedFlag(Player player) {
        // Only capture the flag if the player is the holder
        if (player == redFlagHolder) {
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            isRedFlagStolen = false;
            isRedFlagAtBase = true;
            redFlagHolder = null;

            // Move the flag to the base
            redFlagLocation = redBaseLocation.clone();
            redFlagLocation.setY(redFlagLocation.getY() + 1);
            redFlagLocation.getBlock().setType(Material.RED_BANNER);

            // Increase the scores
            Score playeScore = flagsCaptured.getScore(player.getName());
            Score teamScore = flagsCaptured.getScore(getPlayerTeam(player).getName() + " team");
            playeScore.setScore(playeScore.getScore() + 1);
            teamScore.setScore(teamScore.getScore() + 1);
        }
    }


    public void captureBlueFlag(Player player) {
        // Only capture the flag if the player is the holder
        if (player == blueFlagHolder) {
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            isBlueFlagStolen = false;
            isBlueFlagAtBase = true;
            blueFlagHolder = null;

            // Move the flag to the base
            blueFlagLocation = blueBaseLocation.clone();
            blueFlagLocation.setY(blueFlagLocation.getY() + 1);
            blueFlagLocation.getBlock().setType(Material.BLUE_BANNER);

            // Increase the scores
            Score playeScore = flagsCaptured.getScore(player.getName());
            Score teamScore = flagsCaptured.getScore(getPlayerTeam(player).getName() + " team");
            playeScore.setScore(playeScore.getScore() + 1);
            teamScore.setScore(teamScore.getScore() + 1);
        }
    }


    public boolean playerHasRedFlag(Player player) {
        return (player == redFlagHolder);
    }


    public boolean playerHasBlueFlag(Player player) {
        return (player == blueFlagHolder);
    }


    public boolean isRedFlagAtBase() {
        return isRedFlagAtBase;
    }

    
    public boolean isBlueFlagAtBase() {
        return isBlueFlagAtBase;
    }


    public boolean isRedFlagStolen() {
        return isRedFlagStolen;
    }


    public boolean isBlueFlagStolen() {
        return isBlueFlagStolen;
    }


    public boolean isRedFlagDropped() {
        return (!isRedFlagAtBase && !isRedFlagStolen);
    }


    public boolean isBlueFlagDropped() {
        return (!isBlueFlagAtBase && !isBlueFlagStolen);
    }


    public PlayerClass getPlayerClass(Player player) {
        return PlayerClass.getById(playerClasses.getScore(player.getName()).getScore());
    }


    public void setPlayerClass(Player player, PlayerClass playerClass) {
        playerClasses.getScore(player.getName()).setScore(playerClass.getId());
    }


    public PlayerTeam getPlayerTeam(Player player) {
        if (redTeam.getEntries().contains(player.getName())) {
            return PlayerTeam.RED;
        } else if (blueTeam.getEntries().contains(player.getName())) {
            return PlayerTeam.BLUE;
        } else {
            return PlayerTeam.NONE;
        }
    }


    public void setPlayerTeam(Player player, PlayerTeam playerTeam) {
        if (playerTeam == PlayerTeam.RED) {
            redTeam.addEntry(player.getName());
            blueTeam.removeEntry(player.getName());
        }

        if (playerTeam == PlayerTeam.BLUE) {
            blueTeam.addEntry(player.getName());
            redTeam.removeEntry(player.getName());
        }

        if (playerTeam == PlayerTeam.NONE) {
            redTeam.removeEntry(player.getName());
            blueTeam.removeEntry(player.getName());
        }
    }


    public int getFlagsCaptured(Player player) {
        return flagsCaptured.getScore(player.getName()).getScore();
    }


    public int getFlagsCaptured(PlayerTeam team) {
        return flagsCaptured.getScore(team.getName() + " team").getScore();
    }

    
    public int getFlagsRecovered(Player player) {
        return flagsRecovered.getScore(player.getName()).getScore();
    }


    public int getFlagsRecovered(PlayerTeam team) {
        return flagsRecovered.getScore(team.getName() + " team").getScore();
    }


    public void showScore() {
        showScore(ChatColor.GOLD + "SCORE");
    }


    public void showScore(String customTitle) {
        // Display big title pop up with the scores
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(customTitle, ChatColor.RED + "" + getFlagsCaptured(PlayerTeam.RED) + "" + ChatColor.WHITE + " - " + ChatColor.BLUE + "" + getFlagsCaptured(PlayerTeam.BLUE), 10, 80, 20);
        }
    }


    // ########################################################
    // ##### Provide encapsulation for the private fields #####
    // ########################################################



    /**
     * Gets the location of the red team's base
     * @return The location of the red team's base
     */
    public Location getRedBaseLocation() {
        return redBaseLocation;
    }


    /**
     * Sets the location of the red team's base
     * @param redBaseLocation The location of the red team's base
     */
    public void setRedBaseLocation(Location redBaseLocation) {
        this.redBaseLocation = redBaseLocation;
        isRedBaseLocationSet = true;
    }


    /**
     * Sets the location of the red team's base
     * @param x The x coordinate of the red team's base
     * @param y The y coordinate of the red team's base
     * @param z The z coordinate of the red team's base
     */
    public void setRedBaseLocation(double x, double y, double z) {
        setRedBaseLocation(new Location(plugin.getServer().getWorlds().get(0), x, y, z));
    }


    /**
     * Gets the location of the blue team's base
     * @return The location of the blue team's base
     */
    public Location getBlueBaseLocation() {
        return blueBaseLocation;
    }


    /**
     * Sets the location of the blue team's base
     * @param blueBaseLocation The location of the blue team's base
     */
    public void setBlueBaseLocation(Location blueBaseLocation) {
        this.blueBaseLocation = blueBaseLocation;
        isBlueBaseLocationSet = true;
    }


    /**
     * Sets the location of the blue team's base
     * @param x The x coordinate of the blue team's base
     * @param y The y coordinate of the blue team's base
     * @param z The z coordinate of the blue team's base
     */
    public void setBlueBaseLocation(double x, double y, double z) {
        setBlueBaseLocation(new Location(plugin.getServer().getWorlds().get(0), x, y, z));
    }


    /**
     * Gets the location of the red team's flag
     * @return The location of the red team's flag
     */
    public Location getRedFlagLocation() {
        return redFlagLocation;
    }


    /**
     * Sets the location of the red team's flag
     * @param redFlagLocation The location of the red team's flag
     */
    public void setRedFlagLocation(Location redFlagLocation) {
        this.redFlagLocation = redFlagLocation;
    }


    /**
     * Sets the location of the red team's flag
     * @param x The x coordinate of the red team's flag
     * @param y The y coordinate of the red team's flag
     * @param z The z coordinate of the red team's flag
     */
    public void setRedFlagLocation(double x, double y, double z) {
        this.redFlagLocation = new Location(plugin.getServer().getWorlds().get(0), x, y, z);
    }


    /**
     * Gets the location of the blue team's flag
     * @return The location of the blue team's flag
     */
    public Location getBlueFlagLocation() {
        return blueFlagLocation;
    }


    /**
     * Sets the location of the blue team's flag
     * @param blueFlagLocation The location of the blue team's flag
     */
    public void setBlueFlagLocation(Location blueFlagLocation) {
        this.blueFlagLocation = blueFlagLocation;
    }


    /**
     * Sets the location of the blue team's flag
     * @param x The x coordinate of the blue team's flag
     * @param y The y coordinate of the blue team's flag
     * @param z The z coordinate of the blue team's flag
     */
    public void setBlueFlagLocation(double x, double y, double z) {
        this.blueFlagLocation = new Location(plugin.getServer().getWorlds().get(0), x, y, z);
    }


    /**
     * Gets the scoreboard being used by the game
     * @return The scoreboard being used by the game
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }


    /**
     * Gets the red team
     * @return The red team
     */
    public Team getRedTeam() {
        return redTeam;
    }


    /**
     * Gets the blue team
     * @return The blue team
     */
    public Team getBlueTeam() {
        return blueTeam;
    }


    /**
     * Gets the objective tracking the number of flags captured
     * @return The objective tracking the number of flags captured
     */
    public Objective getFlagsCaptured() {
        return flagsCaptured;
    }


    /**
     * Gets the objective tracking the number of flags recovered
     * @return The objective tracking the number of flags recovered
     */
    public Objective getFlagsRecovered() {
        return flagsRecovered;
    }


    /**
     * Gets the objective tracking the player's classes
     * @return The objective tracking the player's classes
     */
    public Objective getPlayerClasses() {
        return playerClasses;
    }


    /**
     * Checks if the game is running
     * @return True if the game is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }


    /**
     * Gets the plugin that's running the game
     * @return The plugin that's running the game
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
