package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.ScovillePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

public class CreativeItemListener implements Listener {

    public CreativeItemListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onDamage(ItemSpawnEvent e) {

        if (!e.getEntity().getWorld().equals(Bukkit.getWorld("PlotArea"))) return;

        e.setCancelled(true);

    }

}
