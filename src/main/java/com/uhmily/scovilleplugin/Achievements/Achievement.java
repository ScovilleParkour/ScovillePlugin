package com.uhmily.scovilleplugin.Achievements;

import com.fasterxml.jackson.annotation.*;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.ScovilleObject;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public abstract class Achievement<T> extends ScovilleObject implements Listener {

    @JsonSetter(nulls = Nulls.SKIP)
    private int place;

    @JsonIgnore
    protected Achievement() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    private float getUnlockRate() {
        List<ScovillePlayer> sp = ScovillePlayer.getPlayers();

        return sp.stream().filter(scovillePlayer -> scovillePlayer.hasAchievement(this)).count() / (float)sp.size();
    }

    protected String[] getLore(Player p) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        final String RATE_BARS = "▮";
        final int RATE_BAR_COUNT = 20;
        float unlockRate = getUnlockRate();
        return new String[]{
                ChatColor.GRAY + (sp.hasAchievement(this) ? ChatHelper.format(getDesc(), p) : "???"),
                "",
                ChatColor.GRAY + ChatHelper.format("achievement_unlock_rate", p),
                ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + StringUtils.repeat(RATE_BARS, Math.round(unlockRate)) + ChatColor.GRAY + StringUtils.repeat(RATE_BARS, RATE_BAR_COUNT - Math.round(unlockRate)) + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + new DecimalFormat("0.00").format(unlockRate * 100.0f) + "%",
                "",
        };
    }

    @EventHandler
    public abstract void trigger(T event);

    public abstract AchievementDiff getAchievementDiff();

    public abstract String getName();

    public abstract String getDesc();

    public abstract String getAchievement();

    protected void save() {
        AchievementManager.updateAchievement(this);
    }

    @JsonCreator
    public static Achievement<?> getInstance(@JsonProperty("type") String type) {
        Class<Achievement<?>> clazz;
        try {
            clazz = (Class<Achievement<?>>) Class.forName(type);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        Optional<Achievement<?>> achievement = AchievementManager.getAchievements().stream().filter(ach -> ach.getClass().equals(clazz)).findFirst();
        if (achievement.isPresent())
            return achievement.get();
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Achievement<?> getInstance(Class<? extends Achievement<?>> clazz) {
        Optional<Achievement<?>> achievement = AchievementManager.getAchievements().stream().filter(ach -> ach.getClass().equals(clazz)).findFirst();
        if (achievement.isPresent())
            return achievement.get();
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

}
