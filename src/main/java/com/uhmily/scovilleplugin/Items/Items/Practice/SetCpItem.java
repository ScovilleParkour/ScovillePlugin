package com.uhmily.scovilleplugin.Items.Items.Practice;

import com.uhmily.scovilleplugin.Items.HotbarItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetCpItem extends HotbarItem {

    public SetCpItem() {
        super(new ItemStack(Material.SLIME_BALL));
    }

    public static void onClick(Player p) {
        p.performCommand("prac cp");
    }

}
