package com.uhmily.scovilleplugin.Items;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public abstract class HotbarItem extends ItemStack {

    public HotbarItem(ItemStack i) {
        super(i);
    }

    public ItemStack getInstance() {
        NBTItem nbtItem = new NBTItem(this);
        nbtItem.setString("HotbarItem", this.getClass().getName());
        return nbtItem.getItem();
    }

    public static boolean isHotbarItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasKey("HotbarItem");
    }

}
