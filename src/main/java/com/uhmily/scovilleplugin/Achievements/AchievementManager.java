package com.uhmily.scovilleplugin.Achievements;

import com.uhmily.scovilleplugin.Achievements.Achievements.Easy.*;
import com.uhmily.scovilleplugin.Achievements.Achievements.Hard.FlavortownAchievement;
import com.uhmily.scovilleplugin.Achievements.Achievements.Hard.IGNAchievement;
import com.uhmily.scovilleplugin.Achievements.Achievements.Hard.MatchlessAchievement;
import com.uhmily.scovilleplugin.Achievements.Achievements.Medium.*;

import java.util.ArrayList;
import java.util.Optional;

public class AchievementManager {

    private static final AchievementManager manager = new AchievementManager();

    private static final ArrayList<Achievement<?>> achievements = new ArrayList<>();

    private AchievementManager() {

    }

    public static AchievementManager getInstance() {
        return manager;
    }

    private void addAchievement(Achievement<?> achievement) {
        if (!achievements.contains(achievement)) achievements.add(achievement);
    }

    public void register() {
        addAchievement(Achievement.getInstance(AddingSomeFlavorAchievement.class));
        addAchievement(Achievement.getInstance(GlobalDominationAchievement.class));
        addAchievement(Achievement.getInstance(LawAbidingCitizenAchievement.class));
        addAchievement(Achievement.getInstance(NotAPyramidSchemeAchievement.class));
        addAchievement(Achievement.getInstance(PlayingFavoritesAchievement.class));
        addAchievement(Achievement.getInstance(PublicPolicyInfluencerAchievement.class));
        addAchievement(Achievement.getInstance(TheJourneyBeginsAchievement.class));
        addAchievement(Achievement.getInstance(WheredYouGoAchievement.class));
        addAchievement(Achievement.getInstance(FlavortownAchievement.class));
        addAchievement(Achievement.getInstance(IGNAchievement.class));
        addAchievement(Achievement.getInstance(MatchlessAchievement.class));
        addAchievement(Achievement.getInstance(AllStarAchievement.class));
        addAchievement(Achievement.getInstance(FirstAchievement.class));
        addAchievement(Achievement.getInstance(HeatingUpAchievement.class));
        addAchievement(Achievement.getInstance(MarvelousMakerAchievement.class));
        addAchievement(Achievement.getInstance(ThatsGottaHurtAchievement.class));
    }

    public static ArrayList<Achievement<?>> getAchievements() {
        return achievements;
    }

    public static Achievement<?> getAchievement(Class<Achievement<?>> clazz) {
        Optional<Achievement<?>> achievement = achievements.stream().filter(a -> a.getClass().equals(clazz)).findFirst();
        return achievement.orElse(null);
    }

    public static void updateAchievement(Achievement<?> achievement) {
        achievements.replaceAll(ach -> ach.getUuid().equals(achievement.getUuid()) ? achievement : ach);
    }

}
