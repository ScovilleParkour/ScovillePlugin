package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class TogglePlayerListener implements Listener {

    public TogglePlayerListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onOtherPlayerJoin(PlayerLoginEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!ScovillePlayer.getPlayer(p).isPlayerVis()) {
                p.hidePlayer(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent e) {
        if (!ScovillePlayer.getPlayer(e.getPlayer()).isPlayerVis()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                e.getPlayer().hidePlayer(p);
            }
        }
    }

}
