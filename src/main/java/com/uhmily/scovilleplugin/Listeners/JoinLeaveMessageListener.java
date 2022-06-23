package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.JoinLeaveMessage;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveMessageListener implements Listener {

    public JoinLeaveMessageListener() { Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance()); }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());

        JoinLeaveMessage message = sp.getMessage();

        if (message == null)
            ChatHelper.broadcastMessage("join_message_default", e.getPlayer().getName());
        else
            ChatHelper.broadcastMessage("join_message_" + message.getIdent(), e.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());

        JoinLeaveMessage message = sp.getMessage();

        if (message == null)
            ChatHelper.broadcastMessage("leave_message_default", e.getPlayer().getName());
        else
            ChatHelper.broadcastMessage("leave_message_" + message.getIdent(), e.getPlayer().getName());
    }

}
