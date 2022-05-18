package com.lairon.plugin.escapecircle.circles;

import com.lairon.plugin.escapecircle.registered.Circle;
import com.lairon.plugin.escapecircle.registered.CircleRegistered;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BossBarCircle extends Circle {

    private BossBar bar;

    public BossBarCircle(Plugin main, CircleRegistered circleRegistered) {
        super(main, circleRegistered);
        bar = Bukkit.createBossBar("§eВыбраться из Круга", BarColor.GREEN, BarStyle.SEGMENTED_6);
        bar.setProgress(0);
    }

    @Override
    public void start(Player player) {
        bar.addPlayer(player);
    }

    public void setProgress(int progress){
        bar.setProgress(((double) progress) / 6d);
    }

    @Override
    public void onDisable() {
        bar.removeAll();
    }
}
