package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.ScovillePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    public DamageListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();

        if (e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT) || e.getCause().equals(EntityDamageEvent.DamageCause.HOT_FLOOR)) {
            e.setDamage(0.0f);
            p.setHealth(20.0f);
        } else {
            e.setCancelled(true);
        }

    }

}
