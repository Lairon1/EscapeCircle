package com.lairon.plugin.escapecircle.circles;

import com.lairon.plugin.escapecircle.registered.Circle;
import com.lairon.plugin.escapecircle.registered.CircleRegistered;
import com.lairon.plugin.escapecircle.utils.EffectUtils;
import com.lairon.plugin.escapecircle.utils.ItemStackUtils;
import com.lairon.plugin.escapecircle.utils.LocationUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class VillageCircle extends Circle {

    private Location arrowLoc = new Location(Bukkit.getWorld("world"), -278.5, 176.5, -189.5);
    private boolean isDrawArrow = true;
    private Villager villager;
    private IronGolem golem;
    private boolean isStarted = false;
    private Location wheat = new Location(Bukkit.getWorld("world"), -265.5, 176, -217.5);
    private Shulker shulker;

    public VillageCircle(Plugin main, CircleRegistered circleRegistered) {
        super(main, circleRegistered);


        NamespacedKey namespacedKey = new NamespacedKey(getMain(), "customClock");
        ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, new ItemStack(Material.CLOCK));
        shapedRecipe.shape(" h ", "hgh", " b ");
        shapedRecipe.setIngredient('h', Material.HONEYCOMB);
        shapedRecipe.setIngredient('g', Material.GOLDEN_CARROT);
        shapedRecipe.setIngredient('b', Material.BELL);

        if(Bukkit.getRecipe(namespacedKey) != null)
            Bukkit.removeRecipe(namespacedKey);
        Bukkit.addRecipe(shapedRecipe);


    }

    @Override
    public void start(Player player) {
        if(isStarted) return;
        isStarted = true;
        getCircleRegistered().getCircle(BossBarCircle.class).start(player);
        drawArrow();
        villager = player.getWorld().spawn(new Location(player.getWorld(), -257.39, 178.00, -184.30), Villager.class);
        villager.setCustomName("§dМастер городских Фокусов-покусов");
        villager.setMetadata("focus", new FixedMetadataValue(getMain(), true));
        golem = player.getWorld().spawn(new Location(player.getWorld(), -270.91, 175.94, -201.13), IronGolem.class);
        golem.setHealth(1);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(player.getLocation().clone().subtract(0,1,0).getBlock().getType() == Material.GOLD_BLOCK){
                    itemChallengeStart(player);
                    this.cancel();
                }
            }
        }.runTaskTimer(getMain(), 1, 1);
    }


    private void itemChallengeStart(Player player){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!isStarted){
                    this.cancel();
                    return;
                }

                boolean honey = player.getInventory().contains(Material.HONEYCOMB, 3);
                boolean carrot = player.getInventory().contains(Material.GOLDEN_CARROT);
                boolean bell = player.getInventory().contains(Material.BELL);
                boolean clock = player.getInventory().contains(Material.CLOCK);

                if(honey && carrot && bell && clock) this.cancel();

                StringBuilder message = new StringBuilder();
                message.append("§e" + (honey ? "§m" : "") + "Пчелиные соты ");
                message.append("§6" + (carrot ? "§m" : "") + "Золотая морковь ");
                message.append("§a" + (bell ? "§m" : "") + "Колокол ");
                message.append("§d" + (clock ? "§m" : "") + "Ч§d§kас§dы?");

                player.sendActionBar(message.toString());


                if(honey){
                    if(shulker == null){
                        shulker = wheat.getWorld().spawn(wheat.clone(), Shulker.class);
                        shulker.setInvisible(true);
                        shulker.setGlowing(true);
                        shulker.setAI(false);
                        shulker.setSilent(true);
                    }
                }

            }
        }.runTaskTimer(getMain(), 1, 1);


    }

    @EventHandler
    public void onVillagerOpen(PlayerInteractAtEntityEvent e){
        if(!e.getRightClicked().hasMetadata("focus")) return;
        e.setCancelled(true);

        Merchant inv = Bukkit.createMerchant(villager.getCustomName());


        ItemStack pickaxe = new ItemStack(Material.WOODEN_PICKAXE);

        ItemStackUtils.setLore(pickaxe, "", "§7Может сломать:", "§8Обсидиан");

        pickaxe.setDurability((short) 58);

        MerchantRecipe recipe = new MerchantRecipe(pickaxe, 1);
        recipe.addIngredient(new ItemStack(Material.CLOCK));
        recipe.addIngredient(new ItemStack(Material.WHEAT_SEEDS));

        ArrayList<MerchantRecipe> recipes = new ArrayList<>();
        recipes.add(recipe);
        inv.setRecipes(recipes);
        e.getPlayer().openMerchant(inv, true);
    }

    @EventHandler
    public void onWheatRemove(BlockBreakEvent e){
        if(e.getBlock().getLocation().distance(wheat) > 2) return;
        shulker.remove();
        ArrayList<Block> blocks = (ArrayList<Block>) LocationUtils.blockRadiusSphere(wheat, 10);
        blocks.removeIf((block -> block.getType() != Material.WHEAT));
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            Bukkit.getScheduler().runTaskLater(getMain(), () -> block.breakNaturally(new ItemStack(Material.STICK)), i);
        }
    }

    @EventHandler
    public void onObsidianRemove(BlockBreakEvent e){
       if(e.getBlock().getType() != Material.OBSIDIAN) return;
       if(!isStarted) return;
       isStarted = false;
       getCircleRegistered().getCircle(HellCircle.class).start(e.getPlayer());
    }

    private void drawArrow(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!isStarted){
                    this.cancel();
                    return;
                }
                Location middlePoint = arrowLoc.clone().add(0,3,0);
                Location firstPoint = arrowLoc.clone().add(0,1.5,-1);
                Location secondPoint = arrowLoc.clone().add(0,1.5,1);

                EffectUtils.drawParticleLine(arrowLoc, middlePoint, 0.3, Particle.VILLAGER_HAPPY);
                EffectUtils.drawParticleLine(arrowLoc, firstPoint, 0.3,  Particle.VILLAGER_HAPPY);
                EffectUtils.drawParticleLine(arrowLoc, secondPoint, 0.3,  Particle.VILLAGER_HAPPY);

            }
        }.runTaskTimer(getMain(), 1, 1);
    }


    @Override
    public void onDisable() {
        if(isStarted){
            villager.remove();
        }

    }
}
