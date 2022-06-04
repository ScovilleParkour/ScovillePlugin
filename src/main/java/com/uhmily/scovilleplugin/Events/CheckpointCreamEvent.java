package com.uhmily.scovilleplugin.Events;

import com.uhmily.scovilleplugin.Types.Course.Course;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheckpointCreamEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Course course;
    private final Player player;

    public CheckpointCreamEvent(Course course, Player player) {
        this.course = course;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Course getCourse() {
        return course;
    }

    public Player getPlayer() {
        return player;
    }

}
