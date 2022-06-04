package com.uhmily.scovilleplugin.Achievements.Achievements.Easy;

import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LawAbidingCitizenAchievement extends Achievement<PlayerInteractEvent> {

    @Override
    @EventHandler
    public void trigger(PlayerInteractEvent e) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Block plateBlock = e.getClickedBlock();
        if (plateBlock.getLocation().equals(new Location(Bukkit.getWorld("courses_released"), 30000, 51, 100147))) sp.unlockAchievement(this);

        sp.save();
        this.save();
    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.EASY;
    }

    @Override
    public String getName() {
        return "achievement_lawabidingcitizen";
    }

    @Override
    public String getDesc() {
        return "achievement_lawabidingcitizen_desc";
    }

    @Override
    public String getAchievement() {
        return "easy/law_abiding_citizen";
    }

}
