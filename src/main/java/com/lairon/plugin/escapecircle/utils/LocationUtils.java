package com.lairon.plugin.escapecircle.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

public class LocationUtils {

    public static Collection<Block> blockRadiusSphere(Location loc, int radius) {
        Collection<Block> blocks = new ArrayList<>();
        Block center = loc.getBlock();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block b = center.getRelative(x, y, z);
                    if (center.getLocation().distance(b.getLocation()) <= radius) {
                        blocks.add(b);
                    }
                }

            }
        }
        return blocks;
    }

    public static Location toHigestLocation(Location loc){
        for (int y = 255; y < 0; y++) {
            loc.setY(y);
            if(!loc.getBlock().getType().isAir()) {
                loc.setY(y++);
                return loc;
            }
        }
        return loc;
    }

    public static Collection<Location> blockRadiusEmptySphere(Location loc, int radius, int space) {
        Collection<Location> locations = new ArrayList<>();
        for (int yaw = -180; yaw < 180; yaw += space) {
            loc.setYaw(yaw);
            for (int pitch = -90; pitch < 90; pitch += space) {
                loc.setPitch(pitch);
                locations.add(loc.clone().add(loc.getDirection().multiply(radius)));
            }
        }
        return locations;
    }

    public static Collection<Location> blockRadiusEmptyHSphere(Location loc, int radius, int space) {
        Collection<Location> locations = new ArrayList<>();
        for (int yaw = -180; yaw < 180; yaw += space) {
            loc.setYaw(yaw);
            loc.setPitch(0);
            locations.add(loc.clone().add(loc.getDirection().multiply(radius)));
        }
        return locations;
    }

    public static Collection<Location> blockRadiusEmptyHSphereR(Location loc, int radius, int space) {
        Collection<Location> locations = new ArrayList<>();
        for (int yaw = 180; yaw > -180; yaw -= space) {
            loc.setYaw(yaw);
            loc.setPitch(0);
            locations.add(loc.clone().add(loc.getDirection().normalize().multiply(radius)));
        }
        return locations;
    }

    public static ArrayList<Block> blockCube(Location point1, Location point2) {
        ArrayList<Block> blocks = new ArrayList<>();
        for (int x = point1.getBlockX(); x < point2.getBlockX(); x++)
            for (int y = point1.getBlockY(); y < point2.getBlockX(); y++)
                for (int z = point1.getBlockZ(); z < point2.getBlockZ(); z++)
                    blocks.add(new Location(point1.getWorld(), x, y, z).getBlock());
        return blocks;
    }

    public static ArrayList<Entity> getTargetEntity(Player p, int distance) {
        Location loc = p.getLocation().add(0, 1.5, 0);
        ArrayList<Entity> nearbyEntites = new ArrayList<Entity>();
        for (int i = 0; i < distance; i++) {
            Vector durect = loc.getDirection().multiply(i);
            loc.add(durect);
            nearbyEntites.addAll(loc.getWorld().getNearbyEntities(loc, 1, 1, 1));
            nearbyEntites.remove(p);
            if (nearbyEntites.size() != 0) {
                break;
            }
        }
        return nearbyEntites;
    }

    public static boolean isSimilar(Location loc1, Location loc2){
        if(loc1.getWorld() != loc2.getWorld()) return false;
        if(loc1.getX() != loc2.getX()) return false;
        if(loc1.getY() != loc2.getY()) return false;
        if(loc1.getZ() != loc2.getZ()) return false;
        return true;
    }

}
