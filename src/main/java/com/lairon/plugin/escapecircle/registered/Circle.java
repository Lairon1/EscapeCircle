package com.lairon.plugin.escapecircle.registered;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class Circle implements Listener {

    private Plugin main;
    private CircleRegistered circleRegistered;

    public Circle(Plugin main, CircleRegistered circleRegistered) {
        this.main = main;
        this.circleRegistered = circleRegistered;
    }

    public abstract void start(Player player);

    public void onDisable(){};

    protected Plugin getMain() {
        return main;
    }

    protected CircleRegistered getCircleRegistered() {
        return circleRegistered;
    }

}
