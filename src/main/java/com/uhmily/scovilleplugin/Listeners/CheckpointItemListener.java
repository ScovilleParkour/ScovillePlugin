package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Items.Items.CheckpointItem;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Hotbar.BarItem;
import com.uhmily.scovilleplugin.Types.Hotbar.Hotbar;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CheckpointItemListener implements Listener {

    public CheckpointItemListener() { Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance()); }

    public static void getItem(Player p) {

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        UUID hotbarID = sp.getHotBar();
        if (hotbarID == null) {
            p.getInventory().setItem(1, new CheckpointItem(ItemHelper.createItem("&a&l" + ChatHelper.format("item_checkpoint", p), Material.MAGMA_CREAM)).getInstance());
            return;
        }

        Hotbar hotbar = Hotbar.getHotbar(hotbarID);
        BarItem cpItem = hotbar.getCPItem();
        ItemStack cpStack = cpItem.getItem(p, ChatHelper.format("item_checkpoint", p));
        CheckpointItem checkpointItem = new CheckpointItem(cpStack);

        p.getInventory().setItem(1, checkpointItem.getInstance());

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        getItem(e.getPlayer());

    }

}
