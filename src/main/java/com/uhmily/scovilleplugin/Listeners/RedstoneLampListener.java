package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class RedstoneLampListener implements Listener {

    public RedstoneLampListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onRedstoneLamp(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.REDSTONE_LAMP_OFF) && e.getPlayer().isOnGround()) {
            ScovillePlayer sp = ScovillePlayer.getPlayer(p);
            if (sp.isPracticing()) {
                if (sp.getPracticeCP() == null) {
                    p.sendMessage(ChatHelper.format("no_checkpoint", p));
                    return;
                }
                p.teleport(sp.getPracticeCP());
            } else {
                UUID currentlyPlaying = sp.getCurrentlyPlaying();
                if (currentlyPlaying == null) {
                    p.sendMessage(ChatHelper.format("not_playing_course", p));
                    return;
                }
                sp.checkpoint(currentlyPlaying);
            }
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 100.0F, 100.0F);
        }
    }

}
