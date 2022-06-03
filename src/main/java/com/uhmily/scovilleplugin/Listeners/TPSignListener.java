package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.TPSigns.TPSign;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TPSignListener implements Listener {

    public TPSignListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onTPSignClick(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getState() instanceof Sign)) return;
        if (!TPSign.signExists(block.getLocation())) return;
        TPSign tpSign = TPSign.getSign(block.getLocation());
        Player p = e.getPlayer();
        p.teleport(tpSign.getTpLoc());
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 100.0F, 100.0F);
    }

}
