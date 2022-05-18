package com.lairon.plugin.escapecircle.circles;

import com.lairon.plugin.escapecircle.registered.Circle;
import com.lairon.plugin.escapecircle.registered.CircleRegistered;
import com.lairon.plugin.escapecircle.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class LavaCircle extends Circle {

    private ArrayList<Block> lavaBlocks = new ArrayList<>();
    private int speed = 60;
    private Location lavaLocation = new Location(Bukkit.getWorld("world"), -259, 250, -176);
    public LavaCircle(Plugin main, CircleRegistered circleRegistered) {
        super(main, circleRegistered);

        Bukkit.getPluginCommand("lavaSpeed").setExecutor(this::lavaSpeedCommand);
    }

    private boolean lavaSpeedCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings) {
        speed = Integer.parseInt(strings[0]);
        return false;
    }

    private void setLavaBlocks() {

        LocationUtils.blockRadiusEmptyHSphere(lavaLocation, 10, 10).forEach(b -> {
            Block block = b.getBlock();
            if (block.getType() == Material.AIR) {
                block.setType(Material.LAVA);
                lavaBlocks.add(block);
            }
        });
        lavaLocation.setY(lavaLocation.getY() - 1);
        Bukkit.getScheduler().runTaskLater(getMain(), () -> {
            setLavaBlocks();
        }, speed);
    }

    @EventHandler
    public void lavaFlow(BlockFromToEvent e) {
        if (lavaBlocks.contains(e.getBlock())) {
            e.setCancelled(true);
        }
    }

    @Override
    public void start(Player player) {
        setLavaBlocks();
    }


    @Override
    public void onDisable() {
        getMain().getLogger().info("Пожалуйста не выключайте сервер! Идет отчистка карты от лавы...");
        getMain().getLogger().info("Пожалуйста не выключайте сервер! Идет отчистка карты от лавы...");
        getMain().getLogger().info("Пожалуйста не выключайте сервер! Идет отчистка карты от лавы...");
        getMain().getLogger().info("Пожалуйста не выключайте сервер! Идет отчистка карты от лавы...");

        int allBlockCount = lavaBlocks.size();
        AtomicInteger blockCount = new AtomicInteger(0);


        lavaBlocks.forEach(block -> {
            block.setType(Material.AIR);
            blockCount.getAndIncrement();
            getMain().getLogger().info("Отчищено " + new DecimalFormat("###.##%").format(((double) blockCount.get()) / ( (double) allBlockCount)) + " лавы...");
        });


    }
}
