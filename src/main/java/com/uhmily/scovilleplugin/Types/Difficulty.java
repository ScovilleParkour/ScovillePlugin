package com.uhmily.scovilleplugin.Types;

import org.bukkit.ChatColor;

public enum Difficulty {
    SWEET(ChatColor.DARK_GREEN),
    TANGY(ChatColor.GREEN),
    SAVORY(ChatColor.GREEN),
    ZESTY(ChatColor.YELLOW),
    SPICY(ChatColor.YELLOW),
    HOT(ChatColor.GOLD),
    SIZZLING(ChatColor.GOLD),
    FIERY(ChatColor.RED),
    SCORCHING(ChatColor.RED),
    BLAZING(ChatColor.DARK_RED),
    UNKNOWN(ChatColor.DARK_PURPLE);

    private final ChatColor color;

    Difficulty(ChatColor color) {
        this.color = color;
    }

    public int toInt() {
        return this.ordinal() + 1;
    }

    public static Difficulty toDiff(int diff) {
        return Difficulty.values()[diff];
    }

    public ChatColor getColor() {
        return color;
    }

    public Difficulty nextDiff() {
        switch (this) {
            case UNKNOWN:
                return UNKNOWN;
            case BLAZING:
                return null;
            default:
                return Difficulty.values()[toInt()];
        }
    }

    public Difficulty prevDiff() {
        switch (this) {
            case UNKNOWN:
                return UNKNOWN;
            case SWEET:
                return null;
            default:
                return Difficulty.values()[this.ordinal() - 1];
        }
    }

}
