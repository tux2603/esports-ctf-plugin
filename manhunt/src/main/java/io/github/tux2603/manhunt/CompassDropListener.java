package io.github.tux2603.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CompassDropListener implements Listener {
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {

        ItemStack droppedItems = event.getItemDrop().getItemStack();

        if (droppedItems.getType() == Material.COMPASS && droppedItems.getEnchantments().containsKey(Enchantment.VANISHING_CURSE)) {
            // Keep the compass from falling by destroying it
            Bukkit.broadcastMessage("Hey " + event.getPlayer().getName() + ", don't drop that! It's important!");
            event.getItemDrop().remove();

            // Give the player a new compass
            PlayerInventory inventory = event.getPlayer().getInventory();
            ItemStack compass = new ItemStack(Material.COMPASS, 1);
            compass.addEnchantment(Enchantment.VANISHING_CURSE, 1);
            inventory.addItem(compass);
        }
    }
}