package com.lairon.plugin.escapecircle.registered;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class CircleRegistered {

    private Plugin main;
    private HashMap<Class<Circle>, Circle> circleRegistered = new HashMap<>();


    public CircleRegistered(Plugin main) {
        this.main = main;
    }

    public <T extends Circle> void registerCircle(Class<T> tClass){
        if(circleRegistered.containsKey(tClass))
            throw new IllegalArgumentException("This circle is already registered");
        try {
            Circle circle = (Circle) tClass.getConstructors()[0].newInstance(main, this);
            Bukkit.getPluginManager().registerEvents(circle, main);
            circleRegistered.put((Class<Circle>) tClass, circle);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public <T extends Circle> T getCircle(Class<T> tClass){
        if(!circleRegistered.containsKey(tClass))
            throw new IllegalArgumentException("This circle is not registered");
        return (T) circleRegistered.get(tClass);
    }

    public HashMap<Class<Circle>, Circle> getAllCircle(){
        return new HashMap<>(circleRegistered);
    }

    public void onDisable(){
        circleRegistered.values().forEach(c -> c.onDisable());

    }

}
