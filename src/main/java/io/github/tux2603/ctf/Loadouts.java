package io.github.tux2603.ctf;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

final public class Loadouts {
    public static ItemStack[] getInventory(PlayerClass playerClass) {
        ItemStack[] inventory = new ItemStack[9];

        // Make some item stacks that are shared between multiple classes
        // Stack of 64 golden carrots
        ItemStack goldenCarrots = new ItemStack(Material.GOLDEN_CARROT, 64);

        // Iron sword enchanted with durability 1000 and damage 1
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);


        switch (playerClass) {
        case ASSASSIN:
            // Add the golden carrot stack to the inventory
            inventory[0] = goldenCarrots;

            // Add the sword to the inventory
            inventory[1] = sword;

            // create a netherite sword with durability 1000 and damage 1000
            ItemStack netheriteSword = new ItemStack(Material.NETHERITE_SWORD);
            netheriteSword.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            netheriteSword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1000);
            inventory[2] = netheriteSword;

            // Give the player a single potion of invisibility
            ItemStack invisPotion = new ItemStack(Material.POTION, 1);
            PotionMeta invisPotionMeta = (PotionMeta)invisPotion.getItemMeta();
            invisPotionMeta.setDisplayName(ChatColor.GOLD + "Invisibility");
            invisPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, false), true);
            invisPotion.setItemMeta(invisPotionMeta);
            inventory[3] = invisPotion;

            break;
        case MAGE:
            // Add the golden carrot stack to the inventory
            inventory[0] = goldenCarrots;

            // Give the player four lingering potions of levitation
            ItemStack levitationPotions = new ItemStack(Material.LINGERING_POTION, 4);
            PotionMeta levitationPotionMeta = (PotionMeta)levitationPotions.getItemMeta();
            levitationPotionMeta.setDisplayName(ChatColor.GOLD + "Levitation");
            levitationPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, 600, 1, true, false), true);
            levitationPotionMeta.setColor(Color.WHITE);
            levitationPotions.setItemMeta(levitationPotionMeta);
            inventory[1] = levitationPotions;

            // Give the player four lingering potions of poison
            ItemStack poisonPotions = new ItemStack(Material.LINGERING_POTION, 4);
            PotionMeta poisonPotionMeta = (PotionMeta)poisonPotions.getItemMeta();
            poisonPotionMeta.setDisplayName(ChatColor.RED + "Poison");
            poisonPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 800, 0, true, false), true);
            poisonPotionMeta.setColor(Color.RED);
            poisonPotions.setItemMeta(poisonPotionMeta);
            inventory[2] = poisonPotions;

            // Give the player four splash potions of damage
            ItemStack damagePotions = new ItemStack(Material.SPLASH_POTION, 4);
            PotionMeta damagePotionMeta = (PotionMeta)damagePotions.getItemMeta();
            damagePotionMeta.setDisplayName(ChatColor.DARK_RED + "Damage");
            damagePotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 20, 2, true, false), true);
            damagePotionMeta.setColor(Color.RED);
            damagePotions.setItemMeta(damagePotionMeta);
            inventory[3] = damagePotions;

            // Give the player two splash potions of health
            ItemStack healthPotions = new ItemStack(Material.SPLASH_POTION, 2);
            PotionMeta healthPotionMeta = (PotionMeta)healthPotions.getItemMeta();
            healthPotionMeta.setDisplayName(ChatColor.GREEN + "Health");
            healthPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 600, 0, true, false), true);
            healthPotionMeta.setColor(Color.GREEN);
            healthPotions.setItemMeta(healthPotionMeta);
            inventory[4] = healthPotions;

            // Give the player two lingering potions of glowing
            ItemStack glowingPotions = new ItemStack(Material.LINGERING_POTION, 2);
            PotionMeta glowingPotionMeta = (PotionMeta)glowingPotions.getItemMeta();
            glowingPotionMeta.setDisplayName(ChatColor.GOLD + "Glowing");
            glowingPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0, true, false), true);
            glowingPotionMeta.setColor(Color.YELLOW);
            glowingPotions.setItemMeta(glowingPotionMeta);
            inventory[5] = glowingPotions;

            // Give the player a potion called "probably a bad idea" that has every single potion effect
            ItemStack badIdeaPotion = new ItemStack(Material.SPLASH_POTION, 1);
            PotionMeta badIdeaPotionMeta = (PotionMeta)badIdeaPotion.getItemMeta();
            badIdeaPotionMeta.setDisplayName(ChatColor.DARK_RED + "Probably a Bad Idea");

            // loop over every possible potion effect and add it to the potion meta
            // all effects last a default of 300 ticks
            for (PotionEffectType effectType : PotionEffectType.values()) {
                // if this is the confusion effect, make it last twice of long and have the maximum strength
                if (effectType == PotionEffectType.CONFUSION) {
                    badIdeaPotionMeta.addCustomEffect(new PotionEffect(effectType, 600, 255, true, false), true);
                }

                // if this is the blindness effect, make it only last 20 ticks
                else if (effectType == PotionEffectType.BLINDNESS) {
                    badIdeaPotionMeta.addCustomEffect(new PotionEffect(effectType, 20, 0, true, false), true);
                }

                // else, just add the effect normally
                else {
                    badIdeaPotionMeta.addCustomEffect(new PotionEffect(effectType, 300, 0, true, false), true);
                }
            }

            // Setup the lore for the potion
            ArrayList<String> lore = new ArrayList<String>();
            lore.add("Screw it, a hail mary pass, mix all of the potions together and see what happens");
            lore.add("It's probably a bad idea");
            lore.add("It's not very useful");
            lore.add("It's not very safe");
            lore.add("It smells like a creature made of blue cheese died");
            lore.add(ChatColor.ITALIC + "But it will definitely do something");

            badIdeaPotionMeta.setLore(lore);
            badIdeaPotionMeta.setColor(Color.BLACK);
            badIdeaPotion.setItemMeta(badIdeaPotionMeta);
            inventory[6] = badIdeaPotion;

            break;

        case MEDIC:
            // Add the golden carrot stack to the inventory
            inventory[0] = goldenCarrots;

            // give the player a durability 1000 bow with infinite arrows
            ItemStack medicBow = new ItemStack(Material.BOW, 1);
            medicBow.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            medicBow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
            inventory[1] = medicBow;

            // give the player a single arrow
            ItemStack medicArrow = new ItemStack(Material.ARROW, 1);
            inventory[2] = medicArrow;

            // give the player extra carrots
            inventory[3] = goldenCarrots;

            // give the player eight potions called "healing juice" that have regen, absorption, and glowing
            ItemStack healingJuice = new ItemStack(Material.SPLASH_POTION, 8);
            PotionMeta healingJuiceMeta = (PotionMeta)healingJuice.getItemMeta();
            healingJuiceMeta.setDisplayName(ChatColor.GREEN + "Healing Juice");
            healingJuiceMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 1, true, false), true);
            healingJuiceMeta.addCustomEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 1, true, false), true);
            healingJuiceMeta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 400, 1, true, false), true);
            healingJuiceMeta.setColor(Color.GREEN);
            healingJuice.setItemMeta(healingJuiceMeta);
            inventory[4] = healingJuice;

            break;

        case NORMAL:
            // give the player carrots and a sword
            inventory[0] = goldenCarrots;
            inventory[1] = sword;

            // give the player an unbreakable infinity bow with arrow damage 1
            ItemStack infinityBow = new ItemStack(Material.BOW, 1);
            infinityBow.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            infinityBow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
            infinityBow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            inventory[2] = infinityBow;

            // give the player a single arrow
            ItemStack infinityArrow = new ItemStack(Material.ARROW, 1);
            inventory[3] = infinityArrow;

            // give the player an ender pearl
            inventory[4] = new ItemStack(Material.ENDER_PEARL, 1);

            // Give the player 16 slowness 3 arrows
            ItemStack slownessArrows = new ItemStack(Material.TIPPED_ARROW, 16);
            PotionMeta slownessArrowsMeta = (PotionMeta)slownessArrows.getItemMeta();
            slownessArrowsMeta.setDisplayName(ChatColor.BLUE + "Slowness Arrows");
            slownessArrowsMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 1200, 3, true, false), true);
            slownessArrowsMeta.setColor(Color.BLUE);
            slownessArrows.setItemMeta(slownessArrowsMeta);
            inventory[5] = slownessArrows;

            break;

        case SCOUT:
            // give the player golden carrots
            inventory[0] = goldenCarrots;

            // give the player a knock back, sharp stick
            ItemStack knockBack = new ItemStack(Material.STICK, 1);
            knockBack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            knockBack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
            inventory[1] = knockBack;

            // give the player an ender pearl
            inventory[2] = new ItemStack(Material.ENDER_PEARL, 1);

            break;

        case TANK:
            // give the player golden carrots and a sword
            inventory[0] = goldenCarrots;
            inventory[1] = sword;

            // give the player a quick charge 4 crossbow
            ItemStack quickCharge = new ItemStack(Material.CROSSBOW, 1);
            quickCharge.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            quickCharge.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 4);
            inventory[2] = quickCharge;

            // give the player two full stacks of arrows
            ItemStack fullArrows = new ItemStack(Material.ARROW, 64);
            inventory[3] = fullArrows;
            inventory[4] = fullArrows;

            // Give the player 4 fireworks with Green and Gold explosions
            ItemStack fireworks = new ItemStack(Material.FIREWORK_ROCKET, 4);
            FireworkMeta fireworksMeta = (FireworkMeta)fireworks.getItemMeta();
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(4, 106, 40)).with(FireworkEffect.Type.BALL_LARGE).build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(203, 160, 82)).with(FireworkEffect.Type.BALL_LARGE).build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(4, 106, 40)).with(FireworkEffect.Type.BURST).build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(203, 160, 82)).with(FireworkEffect.Type.BURST).build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(4, 106, 40)).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(203, 160, 82)).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(4, 106, 40)).with(FireworkEffect.Type.BURST).withFlicker().build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(203, 160, 82)).with(FireworkEffect.Type.BURST).withFlicker().build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(4, 106, 40)).with(FireworkEffect.Type.BALL_LARGE).withTrail().build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(203, 160, 82)).with(FireworkEffect.Type.BALL_LARGE).withTrail().build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(4, 106, 40)).with(FireworkEffect.Type.BURST).withTrail().build());
            fireworksMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(203, 160, 82)).with(FireworkEffect.Type.BURST).withTrail().build());
            fireworks.setItemMeta(fireworksMeta);
            inventory[5] = fireworks;

            break;

        case NONE: case DEAD:
            // give the player nothing
            break;
        }

        // return the inventory
        return inventory;
    }

    public static ItemStack[] getArmor(PlayerClass playerClass) {
        // create an array to hold the armor
        // index 0 is boots
        // index 1 is leggings
        // index 2 is chest plate
        // index 3 is the helmet
        ItemStack[] armor = new ItemStack[4];

        // All players have a leather chest plate
        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        leatherChestplate.addEnchantment(Enchantment.BINDING_CURSE, 1);
        leatherChestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
        armor[2] = leatherChestplate;

        switch (playerClass) {
        case ASSASSIN:
            // assassin has no other armor
            break;
        case MAGE:
            // mage also has no other armor
            break;
        case MEDIC:
            // medic has leather leggings and feather falling boots
            ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
            leatherLeggings.addEnchantment(Enchantment.BINDING_CURSE, 1);
            leatherLeggings.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            armor[1] = leatherLeggings;

            ItemStack featherFallingBoots = new ItemStack(Material.LEATHER_BOOTS, 1);
            featherFallingBoots.addEnchantment(Enchantment.BINDING_CURSE, 1);
            featherFallingBoots.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            featherFallingBoots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
            armor[0] = featherFallingBoots;
            break;
        case NORMAL:
            // normal players have iron leggings with protection and iron boots
            ItemStack ironLeggings = new ItemStack(Material.IRON_LEGGINGS, 1);
            ironLeggings.addEnchantment(Enchantment.BINDING_CURSE, 1);
            ironLeggings.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            ironLeggings.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
            armor[1] = ironLeggings;
            ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS, 1);
            ironBoots.addEnchantment(Enchantment.BINDING_CURSE, 1);
            ironBoots.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            armor[0] = ironBoots;
        case SCOUT:
            // scout has no extra armor
            break;
        case TANK:
            // tank has diamond helmet, diamond leggings, and diamond boots
            // leggings have blast protection and helmet has projectile protection
            ItemStack diamondHelmet = new ItemStack(Material.DIAMOND_HELMET, 1);
            diamondHelmet.addEnchantment(Enchantment.BINDING_CURSE, 1);
            diamondHelmet.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            diamondHelmet.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
            armor[3] = diamondHelmet;
            ItemStack diamondLeggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
            diamondLeggings.addEnchantment(Enchantment.BINDING_CURSE, 1);
            diamondLeggings.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            diamondLeggings.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
            armor[1] = diamondLeggings;
            ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS, 1);
            diamondBoots.addEnchantment(Enchantment.BINDING_CURSE, 1);
            diamondBoots.addUnsafeEnchantment(Enchantment.DURABILITY, 1000);
            armor[0] = diamondBoots;
            break;
        case NONE: case DEAD:
            // give the player nothing
            break;
        }
        return armor;
    }

    public static float getSpeed(PlayerClass playerClass) {
        switch (playerClass) {
        case NONE:
        case DEAD:
            return 0;
        case TANK:
            return 0.05f;
        case ASSASSIN:
        case MEDIC:
            return 0.18f;
        case NORMAL:
        case MAGE:
            return 0.2f;
        case SCOUT:
            return 0.3f;
        default:
            return 0;
        }
    }

    public static Collection<PotionEffect> getPotionEffects(PlayerClass playerClass) {
        Collection<PotionEffect> potionEffects = new ArrayList<>();
        switch (playerClass) {
        case TANK:
            potionEffects.add(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 127, false, false));
            break;
        case MEDIC:
            potionEffects.add(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 7, false, false));
            potionEffects.add(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 1, false, false));
            break;
        case MAGE:
            potionEffects.add(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 1, false, false));
            potionEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
            break;
        default:
            break;
        }

        return potionEffects;
    }
}