package io.github.tux2603.ctf;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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
    private Objective playerHasClass;

    private JavaPlugin plugin;

    private boolean running;


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

        playerHasClass = scoreboard.getObjective("playerHasClass");
        if (playerHasClass == null) {
            playerHasClass = scoreboard.registerNewObjective("playerHasClass", "dummy", "Player Class is Set");
        }
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
        playerHasClass.unregister();
    }

    /**
     * Starts the game.
     */
    public void start() {
        // Do nothing if the game is already running
        if (running) {
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

    /**
     * Executes a single game tick.
     */
    public void tick() {
        // Do nothing if the game is not running
        if (!running) {
            return;
        }

        // TODO: game logic here
    }


    /**
     * Has a player join a class only if they haven't already joined one
     * @param player The player to have join the class
     * @param className The class to join
     * @return True if the player joined the class, false otherwise
     */
    public void joinClass(Player player, String className) {
        // Do nothing if the game is not running
        if (!running) {
            player.sendMessage(ChatColor.GOLD + "[CTF] " + ChatColor.RED + "The game is not running!");
            return;
        }

        // Check that the player has joined either the red or blue team
        if (!(redTeam.getEntries().contains(player.getName()) || blueTeam.getEntries().contains(player.getName()))) {
            player.sendMessage(ChatColor.GOLD + "[CTF] " + ChatColor.RED + "You must be on a team to join a class!");
            return;
        }

        // Check if the player has already joined a class
        try {
            if (playerHasClass.getScore(player.getName()).getScore() > 0) {
                player.sendMessage(ChatColor.GOLD + "[CTF] " + ChatColor.RED + "You have already joined a class!");
                return;
            }
        } catch (IllegalArgumentException e) {
            // Do nothing, just continue on
        }

        // get the player's class based on the class name
        PlayerClass playerClass = PlayerClass.getByName(className);

        playerHasClass.getScore(player.getName()).setScore(playerClass.getId());

        // Prepare the player inventory to have the necessary items added
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear();


        // If this was not a valid class, then return
        if (playerClass == PlayerClass.NONE) {
            player.sendMessage(ChatColor.GOLD + "[CTF] " + ChatColor.RED + "Invalid class name!");
            return;
        }

        // Add the class items to the player's inventory
        playerInventory.setContents(Loadouts.getInventory(playerClass));

        // Give the player the class's armor
        player.getInventory().setArmorContents(Loadouts.getArmor(playerClass));

        // Set the chestplate color based on the player's team
        ItemStack chestplate = playerInventory.getChestplate();
        LeatherArmorMeta meta = (LeatherArmorMeta)chestplate.getItemMeta();

        if (redTeam.getEntries().contains(player.getName())) {
            meta.setColor(Color.RED);
        }

        if (blueTeam.getEntries().contains(player.getName())) {
            meta.setColor(Color.BLUE);
        }

        chestplate.setItemMeta(meta);
        playerInventory.setChestplate(chestplate);


        player.sendMessage(ChatColor.GOLD + "[CTF] " + ChatColor.GREEN + "You have joined the " + ChatColor.GOLD + className + ChatColor.GREEN + " class!");

        return;
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
    }


    /**
     * Sets the location of the red team's base
     * @param x The x coordinate of the red team's base
     * @param y The y coordinate of the red team's base
     * @param z The z coordinate of the red team's base
     */
    public void setRedBaseLocation(double x, double y, double z) {
        this.redBaseLocation = new Location(plugin.getServer().getWorlds().get(0), x, y, z);
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
    }


    /**
     * Sets the location of the blue team's base
     * @param x The x coordinate of the blue team's base
     * @param y The y coordinate of the blue team's base
     * @param z The z coordinate of the blue team's base
     */
    public void setBlueBaseLocation(double x, double y, double z) {
        this.blueBaseLocation = new Location(plugin.getServer().getWorlds().get(0), x, y, z);
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
