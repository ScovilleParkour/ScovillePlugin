package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.ScovillePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodListener implements Listener {

    public FoodListener() { Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance()); }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

}
