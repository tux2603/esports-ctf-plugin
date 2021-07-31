package io.github.tux2603.ctf;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerRespawnListener implements Listener{
    private GameState gameState;

    public PlayerRespawnListener(GameState gameState) {
        this.gameState = gameState;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // Clear the player's inventory
        event.getPlayer().getInventory().clear();

        // Set the player's class to NONE
        gameState.setPlayerClass(event.getPlayer(), PlayerClass.NONE);

        // Send a message to the player
        event.getPlayer().sendMessage(ChatColor.GOLD + "[CTF] " + ChatColor.RED + "You have respawned! Please click a link to join:");

        // TODO: Include a basic description of the classes
        String blueUnderlined = ChatColor.BLUE + "" + ChatColor.UNDERLINE;

        // Send a link that runs the command to join the normal class
        TextComponent normalLink = new TextComponent("Normal: " + blueUnderlined + "/class normal");
        normalLink.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/class normal"));
        event.getPlayer().spigot().sendMessage(normalLink);

        // Send a link that runs the command to join the scout class
        TextComponent scoutLink = new TextComponent("Scout: " + blueUnderlined + "/class scout");
        scoutLink.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/class scout"));
        event.getPlayer().spigot().sendMessage(scoutLink);

        // Send a link that runs the command to join the tank class
        TextComponent tankLink = new TextComponent("Tank: " + blueUnderlined + "/class tank");
        tankLink.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/class tank"));
        event.getPlayer().spigot().sendMessage(tankLink);

        // Send a link that runs the command to join the assassin class
        TextComponent assassinLink = new TextComponent("Assassin: " + blueUnderlined + "/class assassin");
        assassinLink.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/class assassin"));
        event.getPlayer().spigot().sendMessage(assassinLink);

        // Send a link that runs the command to join the medic class
        TextComponent medicLink = new TextComponent("Medic: " + blueUnderlined + "/class medic");
        medicLink.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/class medic"));
        event.getPlayer().spigot().sendMessage(medicLink);

        // Send a link that runs the command to join the mage class
        TextComponent mageLink = new TextComponent("Mage: " + blueUnderlined + "/class mage");
        mageLink.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/class mage"));
        event.getPlayer().spigot().sendMessage(mageLink);

        event.getPlayer().setGravity(false); 
        Location wayUp = event.getRespawnLocation().add(0, 10000, 0);
        event.setRespawnLocation(wayUp);
    }
}
