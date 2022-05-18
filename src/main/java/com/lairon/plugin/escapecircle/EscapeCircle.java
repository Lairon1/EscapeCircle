package com.lairon.plugin.escapecircle;

import com.lairon.plugin.escapecircle.circles.*;
import com.lairon.plugin.escapecircle.registered.CircleRegistered;
import com.lairon.plugin.escapecircle.utils.PluginOwnerHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class EscapeCircle extends JavaPlugin {

    private CircleRegistered circleRegistered;

    @Override
    public void onEnable() {
        PluginOwnerHolder.setOwner(this);
        circleRegistered = new CircleRegistered(this);
        Bukkit.getWorld("world").setAutoSave(false);

        circleRegistered.registerCircle(LavaCircle.class);
        circleRegistered.registerCircle(BossBarCircle.class);
        circleRegistered.registerCircle(VillageCircle.class);
        circleRegistered.registerCircle(HellCircle.class);
        circleRegistered.registerCircle(AmogusCircle.class);

        Bukkit.getPluginManager().registerEvents(new Listener() {
            private boolean isEnable = false;

            @EventHandler
            public void onBlockBreak(BlockBreakEvent e) {
                if(!isEnable){
                    circleRegistered.getCircle(VillageCircle.class).start(e.getPlayer());
                    isEnable = true;
                }
            }
        }, this);

        Bukkit.getPluginCommand("HallStart").setExecutor((sender, command, label, args) -> {
            if(!(sender instanceof Player)) return false;
            Player player = (Player) sender;

            circleRegistered.getCircle(HellCircle.class).start(player);

            return false;
        });
        Bukkit.getPluginCommand("AmogusStart").setExecutor((sender, command, label, args) -> {
            if(!(sender instanceof Player)) return false;
            Player player = (Player) sender;

            circleRegistered.getCircle(AmogusCircle.class).start(player);

            return false;
        });

    }

    @Override
    public void onDisable() {
        circleRegistered.onDisable();
    }



}
