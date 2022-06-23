package com.uhmily.scovilleplugin.Listeners;

import com.earth2me.essentials.Essentials;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Leveling.LevelManager;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Language;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import net.citizensnpcs.api.CitizensAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScovillePlayerListener implements Listener {

    public ScovillePlayerListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (Bukkit.getPlayer(e.getPlayer().getUniqueId()) == null)
            return;
        Player p = e.getPlayer();
        if (CitizensAPI.getNPCRegistry().getByUniqueIdGlobal(p.getUniqueId()) != null) return;
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        LevelManager.grantPerms(p);
        if (!p.hasPlayedBefore()) {
            String loc = Essentials.getPlugin(Essentials.class).getUser(e.getPlayer()).getGeoLocation();
            if (loc == null) return;
            loc = loc.toLowerCase();
            if (loc.contains("japan")) sp.setLang(Language.JAPANESE);
            else if (loc.contains("germany") || loc.contains("switzerland") || loc.contains("austria")) sp.setLang(Language.GERMAN);
            else if (loc.contains("spain") || loc.contains("mexico")) sp.setLang(Language.SPANISH);
            TextComponent comp = new TextComponent(ChatHelper.format("join_tutorial", p));
            comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial"));
            p.spigot().sendMessage(comp);
        }
    }

}
