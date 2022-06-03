package com.uhmily.scovilleplugin.Types.Hotbar;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BarItem {

    private Material mat;
    private byte dam;
    private String skullURL;
    private boolean isPlayerSkull;
    private ChatColor color;
    private PotionDataWrapped potionData;

    public BarItem() {

        this.mat = Material.STONE;
        this.dam = 0;
        this.skullURL = null;
        this.isPlayerSkull = false;
        this.color = ChatColor.RESET;
        this.potionData = null;

    }

    public BarItem(ItemStack i, Player p) {

        this.mat = i.getType();
        this.dam = i.getData().getData();
        this.skullURL = ItemHelper.getURL(i);
        ItemMeta iMeta = i.getItemMeta();
        Pattern r = Pattern.compile("^&([0-9a-f]).*(?:Checkpoint|Menu|Options)");
        Matcher m = r.matcher(iMeta.getDisplayName() == null ? "" : ChatHelper.reverseToAltColor(iMeta.getDisplayName(), '&'));
        this.color = ChatHelper.charToColor(m.find() ? m.group(1).charAt(0) : ' ');
        if (i.getType().equals(Material.SKULL_ITEM)) {
            SkullMeta skullMeta = (SkullMeta)iMeta;
            this.isPlayerSkull = skullMeta.hasOwner() && skullMeta.getOwner().equals(p.getName());
        } else this.isPlayerSkull = false;
        if (iMeta instanceof PotionMeta) {
            this.potionData = new PotionDataWrapped(((PotionMeta) iMeta).getBasePotionData());
        } else this.potionData = null;

    }

    public BarItem(ItemStack i) {

        this.mat = i.getType();
        this.dam = i.getData().getData();
        this.skullURL = ItemHelper.getURL(i);
        ItemMeta iMeta = i.getItemMeta();
        Pattern r = Pattern.compile("^§([0-9a-f]).*(?:Checkpoint|Menu|Options)");
        Matcher m = r.matcher(iMeta.getDisplayName() == null ? "" : iMeta.getDisplayName());
        this.color = ChatHelper.charToColor(m.find() ? m.group(0).charAt(0) : ' ');
        this.isPlayerSkull = false;
        if (iMeta instanceof PotionMeta) {
            this.potionData = new PotionDataWrapped(((PotionMeta) iMeta).getBasePotionData());
        } else this.potionData = null;

    }

    @JsonIgnore
    public ItemStack getItem(Player p, String name) {
        String title = this.color + "&l" + name;
        ItemStack item;
        if (this.isPlayerSkull) {
            item = ItemHelper.createItem(title, p);
        } else if (this.skullURL != null) {
            item = ItemHelper.createItem(title, this.skullURL);
        } else {
            item = ItemHelper.createItem(title, this.mat, this.dam);
        }
        if (this.potionData != null) {
            PotionMeta potionMeta = (PotionMeta)item.getItemMeta();
            potionMeta.setBasePotionData(this.potionData.toData());
            item.setItemMeta(potionMeta);
        }
        return item;
    }

    public Material getMat() {
        return mat;
    }

    public void setMat(Material mat) {
        this.mat = mat;
    }

    public byte getDam() {
        return dam;
    }

    public void setDam(byte dam) {
        this.dam = dam;
    }

    public String getSkullURL() {
        return skullURL;
    }

    public void setSkullURL(String skullURL) {
        this.skullURL = skullURL;
    }

    public boolean isPlayerSkull() {
        return isPlayerSkull;
    }

    public void setPlayerSkull(boolean playerSkull) {
        isPlayerSkull = playerSkull;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    @JsonIgnore
    public PotionData getPotionData() {
        return potionData.toData();
    }

    @JsonIgnore
    public void setPotionData(PotionData potionData) {
        this.potionData = new PotionDataWrapped(potionData);
    }

    @JsonGetter("potionData")
    private PotionDataWrapped getPotionDataWrapped() {
        return this.potionData;
    }

    @JsonSetter("potionData")
    private void setPotionDataWrapped(PotionDataWrapped potionData) {
        this.potionData = potionData;
    }

}
