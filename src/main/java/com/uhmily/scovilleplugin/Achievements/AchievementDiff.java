package com.uhmily.scovilleplugin.Achievements;

import org.bukkit.ChatColor;

public enum AchievementDiff {

    EASY(ChatColor.GREEN, 5000),
    MEDIUM(ChatColor.GOLD, 10000),
    HARD(ChatColor.RED, 20000),
    TIERED(ChatColor.YELLOW, 0),
    SPECIAL(ChatColor.WHITE, 0);

    private final ChatColor color;
    private final int xp;

    AchievementDiff(ChatColor color, int xp) {
        this.color = color;
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }

    public ChatColor getColor() {
        return color;
    }
}
