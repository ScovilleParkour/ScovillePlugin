package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.YMLHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class ChatColorListener implements Listener {

    private HashMap<String, String> getEmotes(Player p) {
        HashMap<String, String> outMap = new HashMap<>();
        YamlConfiguration configuration = YMLHelper.getYMLConfig("emotes");
        for (String key : configuration.getKeys(false)) {
            if (!p.hasPermission("scoville.emote." + key)) continue;
            ConfigurationSection section = configuration.getConfigurationSection(key);
            outMap.put(section.getString("replace"), section.getString("with"));
        }
        return outMap;
    }

    public ChatColorListener() { Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance()); }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());
        ChatColor color = sp.getChatColor();
        if (color == null) color = ChatColor.WHITE;
        String message = color + e.getMessage();
        for (Map.Entry<String, String> entries : getEmotes(e.getPlayer()).entrySet()) {
            message = message.replace(entries.getKey(), ChatHelper.color(entries.getValue()) + ChatColor.RESET + color);
        }
        e.setMessage(message);
    }

}
