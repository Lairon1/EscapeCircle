package com.lairon.plugin.escapecircle.utils;


import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class ArmorUtils {

    public static boolean isArmor(Player player, ItemStack... armor) {
        return player.getEquipment().getBoots() != null &&
                player.getEquipment().getLeggings() != null &&
                player.getEquipment().getChestplate() != null &&
                player.getEquipment().getHelmet() != null
                && ItemStackUtils.equalsItemsByName(player.getEquipment().getHelmet(), armor[0])
                && ItemStackUtils.equalsItemsByName(player.getEquipment().getChestplate(), armor[1])
                && ItemStackUtils.equalsItemsByName(player.getEquipment().getLeggings(), armor[2])
                && ItemStackUtils.equalsItemsByName(player.getEquipment().getBoots(), armor[3]);
    }



    public static void setArmor(Player player, ItemStack... armor) {
        player.getEquipment().setHelmet(armor[0]);
        player.getEquipment().setChestplate(armor[1]);
        player.getEquipment().setLeggings(armor[2]);
        player.getEquipment().setBoots(armor[3]);
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1, 1);
    }

    public static ShapedRecipe generateRecipeHelmet(Material material, ItemStack armor, String keyString) {
        NamespacedKey chestplateKey = new NamespacedKey(PluginOwnerHolder.getOwner(), keyString);
        ShapedRecipe chestplateRecipe = new ShapedRecipe(chestplateKey, armor);
        chestplateRecipe.shape("iii", "i i", "   ");
        chestplateRecipe.setIngredient('i', material);
        return chestplateRecipe;
    }

    public static ShapedRecipe generateRecipeChestplate(Material material, ItemStack armor, String keyString) {
        NamespacedKey chestplateKey = new NamespacedKey(PluginOwnerHolder.getOwner(), keyString);
        ShapedRecipe chestplateRecipe = new ShapedRecipe(chestplateKey, armor);
        chestplateRecipe.shape("i i", "iii", "iii");
        chestplateRecipe.setIngredient('i', material);
        return chestplateRecipe;
    }

    public static ShapedRecipe generateRecipeLeggings(Material material, ItemStack armor, String keyString) {
        NamespacedKey chestplateKey = new NamespacedKey(PluginOwnerHolder.getOwner(), keyString);
        ShapedRecipe chestplateRecipe = new ShapedRecipe(chestplateKey, armor);
        chestplateRecipe.shape("iii", "i i", "i i");
        chestplateRecipe.setIngredient('i', material);
        return chestplateRecipe;
    }

    public static ShapedRecipe generateRecipeBoots(Material material, ItemStack armor, String keyString) {
        NamespacedKey chestplateKey = new NamespacedKey(PluginOwnerHolder.getOwner(), keyString);
        ShapedRecipe chestplateRecipe = new ShapedRecipe(chestplateKey, armor);
        chestplateRecipe.shape("i i", "i i", "   ");
        chestplateRecipe.setIngredient('i', material);
        return chestplateRecipe;
    }

    //--------------------

    public static ShapedRecipe generateRecipeHelmet(ItemStack material, ItemStack armor, String keyString) {
        NamespacedKey chestplateKey = new NamespacedKey(PluginOwnerHolder.getOwner(), keyString);
        ShapedRecipe chestplateRecipe = new ShapedRecipe(chestplateKey, armor);
        chestplateRecipe.shape("iii", "i i", "   ");
        chestplateRecipe.setIngredient('i', material);
        return chestplateRecipe;
    }

    public static ShapedRecipe generateRecipeChestplate(ItemStack material, ItemStack armor, String keyString) {
        NamespacedKey chestplateKey = new NamespacedKey(PluginOwnerHolder.getOwner(), keyString);
        ShapedRecipe chestplateRecipe = new ShapedRecipe(chestplateKey, armor);
        chestplateRecipe.shape("i i", "iii", "iii");
        chestplateRecipe.setIngredient('i', material);
        return chestplateRecipe;
    }

    public static ShapedRecipe generateRecipeLeggings(ItemStack material, ItemStack armor, String keyString) {
        NamespacedKey chestplateKey = new NamespacedKey(PluginOwnerHolder.getOwner(), keyString);
        ShapedRecipe chestplateRecipe = new ShapedRecipe(chestplateKey, armor);
        chestplateRecipe.shape("iii", "i i", "i i");
        chestplateRecipe.setIngredient('i', material);
        return chestplateRecipe;
    }

    public static ShapedRecipe generateRecipeBoots(ItemStack material, ItemStack armor, String keyString) {
        NamespacedKey chestplateKey = new NamespacedKey(PluginOwnerHolder.getOwner(), keyString);
        ShapedRecipe chestplateRecipe = new ShapedRecipe(chestplateKey, armor);
        chestplateRecipe.shape("i i", "i i", "   ");
        chestplateRecipe.setIngredient('i', material);
        return chestplateRecipe;
    }

}
