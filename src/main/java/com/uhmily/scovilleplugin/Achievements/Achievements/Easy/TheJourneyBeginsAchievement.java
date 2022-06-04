package com.uhmily.scovilleplugin.Achievements.Achievements.Easy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TheJourneyBeginsAchievement extends Achievement<PlayerInteractEvent> {

    @Override
    @EventHandler
    public void trigger(PlayerInteractEvent e) {

        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());
        if (sp == null) return;

        if (sp.hasAchievement(this)) return;
        if (!e.getAction().equals(Action.PHYSICAL)) return;

        Block plateBlock = e.getClickedBlock();
        if (plateBlock.getLocation().equals(new Location(Bukkit.getWorld("courses_released"), -19838, 50, 0))) sp.unlockAchievement(this);

        sp.save();
        this.save();

    }

    @Override
    @JsonIgnore
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.EASY;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return "achievement_thejourneybegins";
    }

    @Override
    @JsonIgnore
    public String getDesc() {
        return "achievement_thejourneybegins_desc";
    }

    @Override
    @JsonIgnore
    public String getAchievement() { return "easy/journey_begins"; }

}
