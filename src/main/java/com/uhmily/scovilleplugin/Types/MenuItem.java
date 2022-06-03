package com.uhmily.scovilleplugin.Types;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MenuItem {

    private Material guiMaterial;
    private short guiMaterialDamage;
    private String skullURL;

    public MenuItem(Material m, short d, String s) {
        this.guiMaterial = m;
        this.guiMaterialDamage = d;
        this.skullURL = s;
    }

    public MenuItem(ItemStack i) {
        if (i.getType() == Material.SKULL_ITEM && i.getData().getData() == 3) {
            this.guiMaterial = Material.SKULL_ITEM;
            this.guiMaterialDamage = 3;
            this.skullURL = ItemHelper.getURL(i);
        } else {
            this.guiMaterial = i.getType();
            this.guiMaterialDamage = i.getData().getData();
            this.skullURL = null;
        }
    }

    public MenuItem(Material m, short d) {
        this(m, d, null);
    }

    public MenuItem() {
        this(Material.STONE, (short)404);
    }

    @Nullable
    public ItemStack toItemStack() {
        if (skullURL != null) {
            return ItemHelper.createItem("", skullURL);
        }
        return new ItemStack(this.guiMaterial, 1, this.guiMaterialDamage);
    }

    @NotNull
    public static String[] getCourseItemLore(Course c, UUID pUUID) {
        final String WLR_BARS = "▮";
        final String RATE_BARS = "✦";
        final int WLR_BAR_COUNT = 20;
        final int RATE_BAR_COUNT = 5;
        final HashMap<Integer, ChatColor> colorMap = new HashMap<Integer, ChatColor>(){{
            put(0, ChatColor.DARK_GRAY);
            put(1, ChatColor.DARK_RED);
            put(2, ChatColor.RED);
            put(3, ChatColor.YELLOW);
            put(4, ChatColor.GREEN);
            put(5, ChatColor.DARK_GREEN);
        }};

        int wlr = Math.round(c.getWLR() * WLR_BAR_COUNT);
        int rate = Math.round(c.getRating());

        return ItemHelper.createLore(
                "&8" + UUIDFetcher.getName(c.getAuthorUUID()),
                c.getDifficulty().getColor() + ChatHelper.format("item_diff_" + c.getDifficulty().toString().toLowerCase(), pUUID) + " - " + c.getDifficulty().toInt() + "/10",
                "",
                ChatHelper.format("item_clear_rate", pUUID),
                ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + StringUtils.repeat(WLR_BARS, wlr) + ChatColor.GRAY + StringUtils.repeat(WLR_BARS, WLR_BAR_COUNT - wlr) + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + new DecimalFormat("0.00").format(c.getWLR() * 100.0f) + "%",
                ChatColor.GRAY + ChatHelper.format("item_user_rating", pUUID) + colorMap.get(rate) + StringUtils.repeat(RATE_BARS, rate) + ChatColor.GRAY + StringUtils.repeat(RATE_BARS, RATE_BAR_COUNT - rate) + " - " + colorMap.get(rate) + new DecimalFormat("0.0").format(c.getRating()),
                "",
                ChatColor.DARK_GRAY + ChatHelper.format("item_times_completed", pUUID) + ChatColor.GRAY + Objects.requireNonNull(ScovillePlayer.getPlayer(pUUID)).getTimesCompleted(c.getUuid()),
                ChatColor.DARK_GRAY + c.getUuid().toString()
        );

    }

    public Material getGuiMaterial() {
        return guiMaterial;
    }

    public void setGuiMaterial(Material guiMaterial) {
        this.guiMaterial = guiMaterial;
    }

    public short getGuiMaterialDamage() {
        return guiMaterialDamage;
    }

    public void setGuiMaterialDamage(short guiMaterialDamage) {
        this.guiMaterialDamage = guiMaterialDamage;
    }

    public String getSkullURL() {
        return skullURL;
    }

    public void setSkullURL(String skullURL) {
        this.skullURL = skullURL;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "guiMaterial=" + guiMaterial +
                ", guiMaterialDamage=" + guiMaterialDamage +
                ", skullURL='" + skullURL + '\'' +
                '}';
    }
}
