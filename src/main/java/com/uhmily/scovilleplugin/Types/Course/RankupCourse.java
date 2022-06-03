package com.uhmily.scovilleplugin.Types.Course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Types.Json.Deserializers.LocationDeserializer;
import com.uhmily.scovilleplugin.Types.Json.Serializers.LocationSerializer;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.Rank;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.track.TrackManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class RankupCourse extends Course {

    private Rank currentRank;
    @JsonSerialize(using = LocationSerializer.class)
    @JsonDeserialize(using = LocationDeserializer.class)
    private Location altJoinLoc;

    protected RankupCourse(UUID uuid) {
        super(uuid);
    }

    public RankupCourse(Course course) {
        super(course.getUuid(), course.getDateCreated(), course.getLeaderboardTimes());
        this.setName(course.getName());
        this.setCourseItem(course.getCourseItem());
        this.setAuthorUUID(course.getAuthorUUID());
        this.setDifficulty(course.getDifficulty());
        this.setCourseWLR(course.getCourseWLR());
        this.setCourseRating(course.getCourseRating());
        this.setColoredName(course.getColoredName());
        this.setStartLoc(course.getStartLoc());
        this.setCourseType(CourseType.RANKUP);
        this.setAltJoinLoc(course.getStartLoc());
    }

    @Override
    public ItemStack toItem(UUID pUUID) {
        final String WLR_BARS = "▮";
        final String RATE_BARS = "✦";
        final int WLR_BAR_COUNT = 20;
        final int RATE_BAR_COUNT = 5;
        final HashMap<Integer, ChatColor> colorMap = new HashMap<Integer, ChatColor>(){{
            put(0, ChatColor.DARK_GRAY);
            put(1, ChatColor.DARK_RED);
            put(2, ChatColor.RED);
            put(3, ChatColor.YELLOW);
            put(4, ChatColor.GREEN);
            put(5, ChatColor.DARK_GREEN);
        }};

        int wlr = Math.round(this.getWLR() * WLR_BAR_COUNT);
        int rate = Math.round(this.getRating());

        return ItemHelper.createItem(this.getColoredName(),
                this.currentRank.toItem(),
                "",
                ChatHelper.format("item_clear_rate", pUUID),
                ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + StringUtils.repeat(WLR_BARS, wlr) + ChatColor.GRAY + StringUtils.repeat(WLR_BARS, WLR_BAR_COUNT - wlr) + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + new DecimalFormat("0.00").format(this.getWLR() * 100.0f) + "%",
                ChatColor.GRAY + ChatHelper.format("item_user_rating", pUUID) + colorMap.get(rate) + StringUtils.repeat(RATE_BARS, rate) + ChatColor.GRAY + StringUtils.repeat(RATE_BARS, RATE_BAR_COUNT - rate) + " - " + colorMap.get(rate) + new DecimalFormat("0.0").format(this.getRating()),
                "",
                ChatColor.DARK_GRAY + ChatHelper.format("item_times_completed", pUUID) + ChatColor.GRAY + Objects.requireNonNull(ScovillePlayer.getPlayer(pUUID)).getTimesCompleted(this.getUuid()),
                ChatColor.DARK_GRAY + this.getUuid().toString()
        );

    }

    @JsonCreator
    public static RankupCourse getCourse(@JsonProperty("uuid") UUID c) {
        Optional<RankupCourse> co = Course.getCourses().stream().filter(course -> course instanceof RankupCourse).filter(course -> course.getUuid().equals(c)).map(course -> (RankupCourse) course).findFirst();
        if (co.isPresent()) {
            return co.get();
        }
        return new RankupCourse(c);
    }

    public static RankupCourse getCourse(String name) {
        Optional<RankupCourse> co = Course.getCourses().stream().filter(course -> course instanceof RankupCourse).filter(course -> course.getName().equalsIgnoreCase(name)).map(course -> (RankupCourse)course).findFirst();
        if (co.isPresent()) {
            return co.get();
        }
        RankupCourse c = new RankupCourse(UUID.randomUUID());
        c.setName(name);
        return c;
    }

    public static RankupCourse getCourseOrNull(Rank r) {
        Optional<RankupCourse> co = Course.getCourses().stream().filter(course -> course instanceof RankupCourse).map(course -> (RankupCourse)course).filter(course -> course.getCurrentRank().equals(r)).findFirst();
        if (co.isPresent()) {
            return co.get();
        }
        return null;
    }

    public static RankupCourse getCourseOrNull(String name) {
        Optional<RankupCourse> co = Course.getCourses().stream().filter(course -> course instanceof RankupCourse).filter(course -> course.getName().equalsIgnoreCase(name)).map(course -> (RankupCourse)course).findFirst();
        if (co.isPresent()) {
            return co.get();
        }
        return null;
    }

    public Rank getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(Rank currentRank) {
        this.currentRank = currentRank;
    }

    public Location getAltJoinLoc() {
        return altJoinLoc;
    }

    public void setAltJoinLoc(Location altJoinLoc) {
        this.altJoinLoc = altJoinLoc;
    }

    @Override
    public void join(Player p) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (currentRank == sp.getRank() || currentRank.toInt() < sp.getRank().toInt()) {
            super.join(p);
            return;
        }
        p.teleport(this.getAltJoinLoc());
        p.sendMessage(ChatHelper.format("join_incorrect_rank", p, this.getCurrentRank().getRankColor() + "&l" + this.getCurrentRank().toString()));
    }

    @Override
    public void end(Player p) {
        super.end(p);
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp.getRank().equals(this.currentRank)) {
            sp.setRank(sp.getRank().nextRank());
            sp.save();
            sp.getRank().applyRank(p);
            RankupCourse currCourse = RankupCourse.getCourseOrNull(sp.getRank());
            if (currCourse != null) {
                currCourse.join(p);
            }
        }
    }

    public static ArrayList<Rank> getAvailableCourses() {
        return Arrays.stream(Rank.values())
                .filter(rank -> !rank.equals(Rank.CAPSAICIN))
                .filter(rank -> Course
                        .getCourses()
                        .stream()
                        .filter(course -> course
                                .getCourseType()
                                .equals(Course.CourseType.RANKUP))
                        .map(course -> (RankupCourse)course)
                        .noneMatch(rankupCourse -> Objects.equals(rankupCourse.getCurrentRank(), rank)))
                .collect(Collectors.toCollection(ArrayList::new)
                );
    }

}
