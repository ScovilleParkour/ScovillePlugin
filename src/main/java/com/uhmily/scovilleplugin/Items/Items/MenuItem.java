package com.uhmily.scovilleplugin.Items.Items;

import com.uhmily.scovilleplugin.Items.HotbarItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuItem extends HotbarItem {

    public MenuItem(ItemStack i) {
        super(i);
    }

    public static void onClick(Player p) {
        p.performCommand("pk menu");
    }

}
