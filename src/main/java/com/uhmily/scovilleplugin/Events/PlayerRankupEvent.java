package com.uhmily.scovilleplugin.Events;

import com.uhmily.scovilleplugin.Types.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRankupEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Rank rank;

    public PlayerRankupEvent(Player player, Rank rank) {
        this.player = player;
        this.rank = rank;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public Rank getRank() {
        return rank;
    }
}