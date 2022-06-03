package com.uhmily.scovilleplugin.Types.Season;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.MenuItem;
import com.uhmily.scovilleplugin.Types.ScovilleObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class Season extends ScovilleObject {

    private static int lastSeason = 0;
    private static final ArrayList<Season> seasons = new ArrayList<>();

    private final int seasonNum;
    private final ArrayList<UUID> courses = new ArrayList<>();
    private MenuItem item = new MenuItem();
    private ChatColor color = ChatColor.WHITE;
    private String name;

    private Season() {
        super();
        seasonNum = ++lastSeason;
        seasons.add(this);
    }

    private Season(UUID uuid, int i) {
        super(uuid);
        seasonNum = i;
        lastSeason = Math.max(i, lastSeason);
        seasons.add(this);
    }

    public ItemStack toItem(UUID pUUID) {
        return ItemHelper.createItem(this.color + ChatHelper.format("season_item_season", pUUID) + this.seasonNum, this.item.toItemStack());
    }

    public void save() {
        seasons.removeIf(season -> season.getUuid().equals(this.getUuid()));
        seasons.add(this);
    }

    public static Season getSeason(int seasonNum) {
        Optional<Season> s = seasons.stream().filter(season -> season.getSeasonNum() == seasonNum).findFirst();
        if (s.isPresent()) {
            return s.get();
        }
        return new Season(UUID.randomUUID(), seasonNum);
    }

    @JsonCreator
    public static Season getSeason(@JsonProperty("seasonNum") int seasonNum, @JsonProperty("courses") ArrayList<UUID> courses) {
        Optional<Season> s = seasons.stream().filter(season -> season.getSeasonNum() == seasonNum).findFirst();
        Season season;
        if (s.isPresent()) {
            season = s.get();
        } else {
            season = new Season(UUID.randomUUID(), seasonNum);
        }
        season.courses.clear();
        season.courses.addAll(courses);
        return season;
    }

    public static Season getSeason(Course c) {
        Optional<Season> s = seasons.stream().filter(season -> season.getCourses().stream().anyMatch(course -> course.getUuid().equals(c.getUuid()))).findFirst();
        if (s.isPresent()) {
            return s.get();
        }
        return null;
    }

    public boolean playerHasPermission(Player p) {
        String name = ScovillePlugin.PERMISSION_NAME + ".season." + this.getSeasonNum();
        return (p.hasPermission(name) && p.isPermissionSet(name)) || p.isOp();
    }

    public static List<Season> getSeasons() {
        return new ArrayList<>(seasons);
    }

    public int getSeasonNum() {
        return seasonNum;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public ArrayList<Course> getCourses() {
        return courses
                .stream()
                .map(Course::getCourse)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @JsonGetter("courses")
    public ArrayList<UUID> getCourseUUIDs() {
        return courses;
    }

    public void addCourse(Course c) {
        if (this.courses.size() >= 10) {
            return;
        }
        this.courses.add(c.getUuid());
    }

    public void removeCourse(Course c) {
        this.courses.remove(c.getUuid());
    }

    public static ArrayList<Season> getAvailableSeasons() {
        return Season.getSeasons().stream()
                .filter(season -> season.getCourses().size() < 10)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void removeCourseStatic(Course c) {
        Season.getSeasons().stream()
                .filter(season -> season.getCourses().stream().anyMatch(course -> course.equals(c)))
                .findFirst()
                .ifPresent(season -> season.removeCourse(c));
    }

    @Override
    public String toString() {
        return "[Season " + this.seasonNum + "] - " + this.courses.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Season)) {
            return false;
        }

        Season s = (Season) o;

        return s.getSeasonNum() == this.seasonNum;
    }

}
