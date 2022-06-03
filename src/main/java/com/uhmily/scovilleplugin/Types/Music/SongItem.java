package com.uhmily.scovilleplugin.Types.Music;

import com.uhmily.scovilleplugin.Types.MenuItem;
import com.uhmily.scovilleplugin.Types.ScovilleObject;
import org.bukkit.ChatColor;

public class SongItem extends ScovilleObject {

    private String name;
    private MenuItem item;
    private ChatColor color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "SongItem{" +
                "name=" + name +
                "item=" + item.toString() +
                "color-" + color.toString() +
                "}";
    }

}
