package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import me.clip.deluxetags.DeluxeTag;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TagJoinListener implements Listener {

    public TagJoinListener()  {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        String tagIdent = ScovillePlayer.getPlayer(e.getPlayer()).getTagIdentifier();
        DeluxeTag tag = DeluxeTag.getLoadedTag(tagIdent);
        if (tag != null) tag.setPlayerTag(e.getPlayer());

    }

}
