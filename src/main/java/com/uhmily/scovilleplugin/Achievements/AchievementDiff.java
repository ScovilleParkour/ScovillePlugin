package com.uhmily.scovilleplugin.Achievements;

import org.bukkit.ChatColor;

public enum AchievementDiff {

    EASY(ChatColor.GREEN),
    MEDIUM(ChatColor.GOLD),
    HARD(ChatColor.RED),
    TIERED(ChatColor.YELLOW),
    SPECIAL(ChatColor.WHITE);

    private final ChatColor color;

    AchievementDiff(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }
}
