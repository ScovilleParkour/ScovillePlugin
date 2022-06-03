package com.uhmily.scovilleplugin.Achievements;

import com.uhmily.scovilleplugin.Achievements.Achievements.PlayingFavoritesAchievement;
import com.uhmily.scovilleplugin.Achievements.Achievements.TheJourneyBeginsAchievement;
import com.uhmily.scovilleplugin.Achievements.Achievements.WheredYouGoAchievement;

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
        addAchievement(Achievement.getInstance(TheJourneyBeginsAchievement.class));
        addAchievement(Achievement.getInstance(PlayingFavoritesAchievement.class));
        addAchievement(Achievement.getInstance(WheredYouGoAchievement.class));
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
