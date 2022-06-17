package com.uhmily.scovilleplugin.Announcements;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class AnnouncementManager {

    private static final AnnouncementManager instance = new AnnouncementManager();

    private AnnouncementManager() {}

    public static AnnouncementManager getInstance() {
        return instance;
    }

    public void announce() {
        String[] messages = ChatHelper.formatArray("announcements", Language.ENGLISH);
        Random random = new Random();
        int index = random.nextInt(messages.length);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatHelper.formatArray("announcements", player)[index]);
        }
    }

    public void startTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(ScovillePlugin.getInstance(), () -> announce(), 0, 20 * 60 * 5);
    }

}
