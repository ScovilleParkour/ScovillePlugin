package com.uhmily.scovilleplugin.Events;

import com.uhmily.scovilleplugin.Types.Course.Course;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Optional;

public class CourseCompleteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Course course;
    private final Player player;
    private final Optional<Long> totalTime;

    public CourseCompleteEvent(Course course, Player player, Optional<Long> totalTime) {
        this.course = course;
        this.player = player;
        this.totalTime = totalTime;
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

    public Optional<Long> getTotalTime() {
        return totalTime;
    }
}
