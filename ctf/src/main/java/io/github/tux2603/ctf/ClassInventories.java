package io.github.tux2603.ctf;

import org.bukkit.inventory.ItemStack;

public enum ClassInventories {
    // All players have a stack of golden carrots
    // The assasin gets a special insta-kill sword, 
    ASSASIN (new ItemStack[] {

    }),

    MAGE (new ItemStack[] {

    }),

    MEDIC (new ItemStack[] {

    }),

    NORMIE (new ItemStack[] {

    }),

    SCOUT (new ItemStack[] {

    }),

    TANK (new ItemStack[] {

    });

    private final ItemStack[] items;
    private ClassInventories(ItemStack[] items) {
        this.items = items;
    }

    public ItemStack[] getItems() {
        return items;
    }
}
