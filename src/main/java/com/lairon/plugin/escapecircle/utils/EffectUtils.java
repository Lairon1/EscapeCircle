package com.lairon.plugin.escapecircle.utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class EffectUtils {

    public static void startFlyingLinesEffect(Location location, int ticks, ParticleBuilder builder) {
        new BukkitRunnable() {
            float step = 0;
            int count = 0;

            @Override
            public void run() {
                if (count >= ticks) {
                    this.cancel();
                    return;
                }
                Vector vector = new Vector();
                for (int i = 0; i < 10; i++) {
                    step++;
                    float t = (float) PI / 150 * step;
                    float r = (float) sin(t) * 1.0F;
                    float s = (float) (PI * 2) * t;
                    vector.setX(1.0F * r * cos(s) + 0.0F);
                    vector.setZ(1.0F * r * sin(s) + 0.0F);
                    vector.setY(2.0F * 1.0F * cos(t) + 0.8F + 1);
                    location.add(vector);
                    builder.location(location)
                            .spawn();
                    location.subtract(vector);
                }
                count++;
            }
        }.runTaskTimer(PluginOwnerHolder.getOwner(), 0, 1);
    }

    public static void startFlyingLinesEffect(Entity entity, int ticks, ParticleBuilder builder) {
        new BukkitRunnable() {
            float step = 0;
            int count = 0;

            @Override
            public void run() {
                if (count >= ticks) {
                    this.cancel();
                    return;
                }
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                Location location = entity.getLocation();
                Vector vector = new Vector();
                for (int i = 0; i < 10; i++) {
                    step++;
                    float t = (float) PI / 150 * step;
                    float r = (float) sin(t) * 1.0F;
                    float s = (float) (PI * 2) * t;
                    vector.setX(1.0F * r * cos(s) + 0.0F);
                    vector.setZ(1.0F * r * sin(s) + 0.0F);
                    vector.setY(2.0F * 1.0F * cos(t) + 0.8F + 1);
                    location.add(vector);
                    builder.location(location)
                            .spawn();
                    location.subtract(vector);
                }
                count++;
            }
        }.runTaskTimer(PluginOwnerHolder.getOwner(), 0, 1);
    }

    public static void startGroundLinesEffect(Entity entity, int ticks, ParticleBuilder builder) {
        new BukkitRunnable() {
            float step = 0;
            int count = 0;

            @Override
            public void run() {
                if (count >= ticks) {
                    this.cancel();
                    return;
                }
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }

                Location location = entity.getLocation();
                Vector vector = new Vector();
                for (int i = 0; i < 10; i++) {
                    step++;
                    float t = (float) PI / 150 * step;
                    float r = (float) sin(t) * 1.0F;
                    float s = (float) (PI * 2) * t;
                    vector.setX(1.0F * r * cos(s) + 0.0F);
                    vector.setZ(1.0F * r * sin(s) + 0.0F);
                    location.add(vector);
                    builder.location(location)
                            .spawn();
                    location.subtract(vector);
                }
                count++;
            }
        }.runTaskTimer(PluginOwnerHolder.getOwner(), 0, 1);
    }

    private static ArrayList<Color> colors = new ArrayList<Color>(){{
        add(Color.BLUE);
        add(Color.LIME);
        add(Color.OLIVE);
        add(Color.ORANGE);
        add(Color.PURPLE);
        add(Color.GREEN);
        add(Color.MAROON);
        add(Color.SILVER);
        add(Color.RED);
        add(Color.FUCHSIA);
        add(Color.YELLOW);
    }};
    private static Random random = new Random();

    public static void startGroundLinesRGBEffect(Entity entity, int ticks) {
        new BukkitRunnable() {
            float step = 0;
            int count = 0;

            @Override
            public void run() {
                if (count >= ticks) {
                    this.cancel();
                    return;
                }
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }

                Location location = entity.getLocation();
                Vector vector = new Vector();
                for (int i = 0; i < 10; i++) {
                    step++;
                    float t = (float) PI / 150 * step;
                    float r = (float) sin(t) * 1.0F;
                    float s = (float) (PI * 2) * t;
                    vector.setX(1.0F * r * cos(s) + 0.0F);
                    vector.setZ(1.0F * r * sin(s) + 0.0F);
                    location.add(vector);
                    Particle.DustOptions dust = new Particle.DustOptions(
                            colors.get(random.nextInt(colors.size())),
                            1
                    );
                    location.getWorld().spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 0,0,0, 0, dust);
                    location.subtract(vector);
                }
                count++;
            }
        }.runTaskTimer(PluginOwnerHolder.getOwner(), 0, 1);
    }


    public static void startGroundLinesEffect(Location location, int ticks, ParticleBuilder builder) {
        new BukkitRunnable() {
            float step = 0;
            int count = 0;

            @Override
            public void run() {
                if (count >= ticks) {
                    this.cancel();
                    return;
                }
                Vector vector = new Vector();
                for (int i = 0; i < 10; i++) {
                    step++;
                    float t = (float) PI / 150 * step;
                    float r = (float) sin(t) * 1.0F;
                    float s = (float) (PI * 2) * t;
                    vector.setX(1.0F * r * cos(s) + 0.0F);
                    vector.setZ(1.0F * r * sin(s) + 0.0F);
                    location.add(vector);
                    builder.location(location)
                            .spawn();
                    location.subtract(vector);
                }
                count++;
            }
        }.runTaskTimer(PluginOwnerHolder.getOwner(), 0, 1);
    }

    public static void drawParticleLine(Location point1, Location point2, double space, Particle particle) {
        World world = point1.getWorld();
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            point2.getWorld().spawnParticle(particle, new Location(world, p1.getX(), p1.getY(), p1.getZ()), 0);
            length += space;
        }
    }

    public static void drawSphere(Location loc, int radius, int space, ParticleBuilder builder) {
        LocationUtils.blockRadiusEmptySphere(loc, radius, space).forEach(l -> builder.location(l).spawn());
    }

    public void drawHeart(Location loc, double maxRadius, ParticleBuilder builder) {
        double r, x, y;
        for (int angle = 0; angle < 50; angle++) {
            r = 1 - sin(angle);
            x = cos(angle) * r * maxRadius + loc.getX();
            y = sin(angle) * r * maxRadius + loc.getY();

            Location add = loc.clone().set(x, y, 0);
            builder.location(add).spawn();
        }
    }
}
    


