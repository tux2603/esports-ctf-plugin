package io.github.tux2603.manhunt;

import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.Team;


public class ManhuntCommands implements CommandExecutor {
    Random random = new Random();

    Team hunted;
    Team hunters;

    public ManhuntCommands(Team hunted, Team hunters) {
        super();
        this.hunted = hunted;
        this.hunters = hunters;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName()) {
        case "start":
            return startCommand(sender, command, label, args);
        default:
            return false;
        }
    }

    private boolean startCommand(CommandSender sender, Command command, String label, String[] args) {
        // Clear any existing entries from both of the teams
        Set<String> huntedEntries = hunted.getEntries();
        Set<String> huntersEntries = hunters.getEntries();

        for (String entry : huntedEntries) {
            hunted.removeEntry(entry);
        }

        for (String entry : huntersEntries) {
            hunters.removeEntry(entry);
        }

        // Select a random player from all of the online players to be the hunted
        Object players[] = Bukkit.getOnlinePlayers().toArray();

        if (players.length == 0) {
            Bukkit.broadcastMessage("There are currently no players online, so there's nobody to play with :(");
        }

        else {
            Player huntedPlayer = (Player)players[random.nextInt(players.length)];
            hunted.addEntry(huntedPlayer.getName());
            Bukkit.broadcastMessage(huntedPlayer.getName() + " is the hunted");

            for (Object o : players) {
                Player p = (Player)o;
                String playerName = p.getName();

                // Clear everyone's inventory
                PlayerInventory inventory = p.getInventory();
                inventory.clear();

                // If the player is a hunter, add them to the team and give them a compass
                if (!hunted.hasEntry(playerName)) {
                    hunters.addEntry(playerName);
                    Bukkit.broadcastMessage(playerName + " is a hunter");

                    // Give the hunter a compass
                    ItemStack compass = new ItemStack(Material.COMPASS, 1);
                    compass.addEnchantment(Enchantment.VANISHING_CURSE, 1);
                    inventory.addItem(compass);
                }
            }
        }

        return true;
    }
}