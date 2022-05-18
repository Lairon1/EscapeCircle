package com.lairon.plugin.escapecircle.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemStackBuilder {

    private ItemStack stack;


    public ItemStackBuilder(Material material) {
        stack = new ItemStack(material);
    }

    public ItemStack build() {
        return stack;
    }


    public ItemStackBuilder name(String name) {
        ItemStackUtils.setDisplayName(stack, name);
        return this;
    }


    public ItemStackBuilder lore(ArrayList<String> lore) {
        ItemStackUtils.setLore(stack, lore);
        return this;
    }

    public ItemStackBuilder lore(String... lore) {
        ItemStackUtils.setLore(stack, lore);
        return this;
    }

    public ItemStackBuilder enchantment(Enchantment enchantment, int lvl) {
        ItemStackUtils.addEnchantment(stack, enchantment, lvl);
        return this;
    }

    public ItemStackBuilder unbreakable() {
        ItemStackUtils.setUnbreakable(stack, true);
        return this;
    }

    public ItemStackBuilder unbreakable(boolean visible) {
        ItemStackUtils.setUnbreakable(stack, visible);
        return this;
    }

    public ItemStackBuilder customModelData(ItemStack itemStack, int data) {
        ItemStackUtils.setCustomModelData(stack, data);
        return this;
    }

}
