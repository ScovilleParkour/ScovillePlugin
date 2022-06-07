package com.uhmily.scovilleplugin.Items.Items.Practice;

import com.uhmily.scovilleplugin.Items.HotbarItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PracticeFlyItem extends HotbarItem {

    public PracticeFlyItem() {
        super(new ItemStack(Material.SUGAR));
    }

    public static void onClick(Player p) {
        p.performCommand("prac fly");
    }

}
