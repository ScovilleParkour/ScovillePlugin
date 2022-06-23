package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.ScovillePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ToggleChatListener implements Listener {

    public ToggleChatListener() { Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance()); }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        /*Player p = e.getPlayer();
        for (Player p2 : e.getRecipients()) {
            if (!(ScovillePlayer.hasPlayer(p2.getUniqueId()) && ScovillePlayer.getPlayer(p2.getUniqueId()).isToggleChat())) continue;
            e.getRecipients().remove(p2);
        }
        if (!(ScovillePlayer.hasPlayer(p.getUniqueId()) && ScovillePlayer.getPlayer(p.getUniqueId()).isToggleChat())) return;
        e.setCancelled(true);*/
    }

}
