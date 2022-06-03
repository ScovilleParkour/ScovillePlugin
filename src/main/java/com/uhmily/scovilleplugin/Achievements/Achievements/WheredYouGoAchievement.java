package com.uhmily.scovilleplugin.Achievements.Achievements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.UUID;

public class WheredYouGoAchievement extends Achievement<AfkStatusChangeEvent> {

    private final HashMap<UUID, Long> timeAfk = new HashMap<>();
    private final HashMap<UUID, Long> timeStartedAfk = new HashMap<>();

    @Override
    @EventHandler
    public void trigger(AfkStatusChangeEvent event) {

        UUID pUUID = event.getAffected().getUUID();
        ScovillePlayer sp = ScovillePlayer.getPlayer(pUUID);
        if (sp == null) return;

        if (sp.hasAchievement(this)) return;

        if (event.getValue()) {
            timeStartedAfk.put(pUUID, System.currentTimeMillis());
        } else {
            long totalTime = timeAfk.getOrDefault(pUUID, 0L);
            totalTime += System.currentTimeMillis() - timeStartedAfk.getOrDefault(pUUID, 0L);
            timeAfk.put(pUUID, totalTime);
            if (totalTime >= 36000000L) {
                sp.unlockAchievement(this);
            }
        }

    }

    @Override
    @JsonIgnore
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.EASY;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return "achievement_wheredyougo";
    }

    @Override
    @JsonIgnore
    public String getSkullURL() {
        return "http://textures.minecraft.net/texture/70d05642b417d5a9b36af257ec38e0325db28fd7106b16a0b89a2f9b7c9d6c0d";
    }

    @Override
    @JsonIgnore
    public String getDesc() {
        return "achievement_wheredyougo_desc";
    }

    @Override
    @JsonIgnore
    public String getAchievement() { return "scoville:easy/whered_you_go"; }

    @Override
    public int getPlace() {
        return 2;
    }

    public HashMap<UUID, Long> getTimeAfk() {
        return timeAfk;
    }

    public HashMap<UUID, Long> getTimeStartedAfk() {
        return timeStartedAfk;
    }
}
