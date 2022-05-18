package com.lairon.plugin.escapecircle.circles;

import com.lairon.plugin.escapecircle.registered.Circle;
import com.lairon.plugin.escapecircle.registered.CircleRegistered;
import com.lairon.plugin.escapecircle.utils.ItemStackBuilder;
import com.lairon.plugin.escapecircle.utils.ItemStackUtils;
import com.lairon.plugin.escapecircle.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class AmogusCircle extends Circle {


    private Location startLoc = new Location(Bukkit.getWorld("world"), -295, 105, -207);
    private Location lineLocation = new Location(Bukkit.getWorld("world"), -270, 105, -207);
    private Location gameLocation = new Location(Bukkit.getWorld("world"), -250, 103, -174);
    private Location chestLocation = new Location(Bukkit.getWorld("world"), -256, 105, -166);

    private Location standLocation = new Location(Bukkit.getWorld("world"), -251.5, 103, -171.5, 175, 0);

    private boolean isStarted = false;
    private Random random = new Random();
    private ArmorStand stand;


    public AmogusCircle(Plugin main, CircleRegistered circleRegistered) {
        super(main, circleRegistered);
    }

    @Override
    public void start(Player player) {
        isStarted = true;
        getCircleRegistered().getCircle(BossBarCircle.class).start(player);
        getCircleRegistered().getCircle(BossBarCircle.class).setProgress(2);
        player.setGravity(false);
        player.setAllowFlight(true);
        player.setVelocity(new Vector(0,0,0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255));
        player.teleport(startLoc);

        ItemStack apple = new ItemStackBuilder(Material.APPLE)
                .name("§cЯблоко предателя")
                .build();

        ItemStack goldenApple = new ItemStackBuilder(Material.GOLDEN_APPLE)
                .name("§eЗолотое Яблоко предателя")
                .build();
        ItemStack bed = new ItemStackBuilder(Material.RED_BED)
                .name("§cКровать предателя")
                .build();

        player.getInventory().addItem(apple, goldenApple, bed);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(player.getLocation().distance(lineLocation) < 3){
                    player.setAllowFlight(false);
                    player.setGravity(true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255));
                    player.teleport(gameLocation);
                    stand = (ArmorStand) standLocation.getWorld().spawnEntity(standLocation, EntityType.ARMOR_STAND);


                    this.cancel();
                    return;
                }
                player.setVelocity(lineLocation.clone().subtract(player.getLocation().clone()).clone().toVector().normalize().multiply(0.2));
            }
        }.runTaskTimer(getMain(), 1, 1);
        
    }
    
    
    @EventHandler
    public void onChestOpen(PlayerInteractEvent e){
        if(e.getClickedBlock() == null) return;
        if(!LocationUtils.isSimilar(chestLocation, e.getClickedBlock().getLocation())) return;
        if(e.getItem() == null || e.getItem().getType() == null || e.getItem().getType() != Material.TRIPWIRE_HOOK){
            e.setCancelled(true);
            e.getPlayer().playSound(e.getClickedBlock().getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1, 1);
        }
    }
}
