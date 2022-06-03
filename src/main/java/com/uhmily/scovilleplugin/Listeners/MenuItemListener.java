package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Items.Items.MenuItem;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Hotbar.Hotbar;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class MenuItemListener implements Listener {

    public MenuItemListener() { Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance()); }

    public static void getItem(Player p) {

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        UUID hotbarID = sp.getHotBar();
        if (hotbarID == null) {
            p.getInventory().setItem(4, new MenuItem(ItemHelper.createItem("&f&l" + ChatHelper.format("item_menu", p), Material.NETHER_STAR)).getInstance());
            return;
        }

        p.getInventory().setItem(4, new MenuItem(Hotbar.getHotbar(hotbarID).getMenuItem().getItem(p, ChatHelper.format("item_menu", p))).getInstance());

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        getItem(e.getPlayer());

    }

}
