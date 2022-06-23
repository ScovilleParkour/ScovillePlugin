package com.uhmily.scovilleplugin.Types.Course;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Events.CourseJoinEvent;
import com.uhmily.scovilleplugin.GUI.Inventories.RateInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Items.HotbarItem;
import com.uhmily.scovilleplugin.Leveling.LevelManager;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Difficulty;
import com.uhmily.scovilleplugin.Types.Json.Deserializers.LocationDeserializer;
import com.uhmily.scovilleplugin.Types.Json.Serializers.LocationSerializer;
import com.uhmily.scovilleplugin.Types.MenuItem;
import com.uhmily.scovilleplugin.Types.Music.Song;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.ScovilleObject;
import com.uhmily.scovilleplugin.Types.Season.Season;
import com.uhmily.scovilleplugin.Types.WinMessage.ModifiedWinMessage;
import me.clip.deluxetags.DeluxeTag;
import me.clip.deluxetags.DeluxeTags;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.matcher.NodeMatcher;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Course extends ScovilleObject {

    public enum CourseType {
        HALLWAY,
        OPEN,
        RANKUP
    }

    private static final List<Course> courses = new ArrayList<>();

    private String name;
    private MenuItem menuItem;
    private UUID authorUUID;
    private Difficulty difficulty;
    private CourseWLR courseWLR;
    private CourseRating courseRating;
    protected final long dateCreated;
    private String coloredName;
    @JsonSerialize(using = LocationSerializer.class)
    @JsonDeserialize(using = LocationDeserializer.class)
    private Location startLoc;
    private CourseType courseType;
    private final HashMap<UUID, Long> leaderboardTimes;

    protected Course(UUID uuid) {
        this(uuid, System.currentTimeMillis(), new HashMap<>());
    }

    protected Course(UUID uuid, long dateCreated, HashMap<UUID, Long> lT) {
        super(uuid);
        this.dateCreated = dateCreated;
        this.courseWLR = new CourseWLR();
        this.courseRating = new CourseRating();
        this.leaderboardTimes = lT;
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public ItemStack toItem(UUID pUUID) {
        return ItemHelper.createItem(this.coloredName, this.menuItem.toItemStack(), MenuItem.getCourseItemLore(this, pUUID));
    }

    public ItemStack toSongItem(UUID pUUID) {
        Pattern r = Pattern.compile("^((?:&[0-9a-fk-or])+)");
        Matcher m = r.matcher(this.coloredName);
        String prefix = m.find() ? m.group(0) : "";
        Song song = Song.getSong(this.getName());
        String author = song.getAuthor();
        if (author.equals("")) return null;
        Song currSong = Objects.requireNonNull(ScovillePlayer.getPlayer(pUUID)).getCurrentSong();
        return ItemHelper.createItem(prefix + "♪ " + this.coloredName, this.menuItem.toItemStack(),
                ChatColor.GRAY + ChatHelper.format(author.contains(", ") ? "song_item_author" : "song_item_authors", pUUID) + author,
                ChatHelper.format(currSong != null && currSong.getName().equals(song.getName()) ? "create_selected" : "create_not_selected", pUUID)
        );
    }

    @JsonCreator
    public static Course getCourse(@JsonProperty("uuid") UUID c) {
        Optional<Course> co = courses.stream().filter(course -> course.getUuid().equals(c)).findFirst();
        if (co.isPresent()) {
            return co.get();
        }
        return new Course(c);
    }

    public static Course getCourse(String name) {
        Optional<Course> co = courses.stream().filter(course -> course.getName().equalsIgnoreCase(name)).findFirst();
        if (co.isPresent()) {
            return co.get();
        }
        Course c = new Course(UUID.randomUUID());
        c.setName(name);
        return c;
    }

    public static Course getCourseOrNull(String name) {
        Optional<Course> co = courses.stream().filter(course -> course.getName().equalsIgnoreCase(name)).findFirst();
        if (co.isPresent()) {
            return co.get();
        }
        return null;
    }

    public static void removeCourse(UUID courseUUID) {
        courses.removeIf(course -> course.getUuid().equals(courseUUID));
    }

    public void save() {
        courses.removeIf(course -> course.getUuid().equals(this.getUuid()));
        courses.add(this);
    }

    public void join(Player p) {

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp == null) return;
        sp.stopSong();
        sp.setCurrentlyPlaying(this.getUuid());
        if (!sp.hasPlayed(this.getUuid())) {
            sp.addPlayed(this.getUuid());
            CourseWLR courseWLR = this.getCourseWLR();
            courseWLR.setLosses(this.getCourseWLR().getLosses() + 1);
            this.setCourseWLR(courseWLR);
            this.save();
        }
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null || item.getType().equals(Material.AIR)) continue;
            if (HotbarItem.isHotbarItem(item)) continue;
            p.getInventory().setItem(i, null);
        }
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.NIGHT_VISION))
                continue;
            p.removePotionEffect(effect.getType());
        }
        p.setFlying(false);
        p.setAllowFlight(false);
        sp.setPracticeCP(null);
        if (sp.isPracticing())
            p.sendMessage(ChatHelper.format("isnt_practicing", p));
        sp.setPracticing(false);
        sp.save();

        CourseJoinEvent courseJoinEvent = new CourseJoinEvent(this, p);
        Bukkit.getPluginManager().callEvent(courseJoinEvent);

        if (sp.getCheckpoints().containsKey(this.getUuid()) && sp.isStartAtCP())
            p.teleport(sp.getCheckpoint(this.getUuid()));
        else
            p.teleport(this.startLoc);
        p.sendMessage(ChatHelper.format("course_joined", p, this.coloredName));
    }

    private Map.Entry<UUID, Long> getBestTime() {
        return new AbstractMap.SimpleEntry<>(this.getLeaderboardTimes().entrySet().stream().min(Map.Entry.comparingByValue()).get());
    }

    private Map.Entry<UUID, Integer> getMostTimes() {
        return ScovillePlayer.getPlayers().stream().map((player) -> new AbstractMap.SimpleEntry<>(player.getUuid(), player.getTimesCompleted(this.getUuid()))).max(Map.Entry.comparingByValue()).get();
    }

    public void end(Player p) {

        ScovillePlayer player = ScovillePlayer.getPlayer(p);
        if (player == null) return;
        Long startTime = player.getStartTime(this.getUuid());
        Long totalTime = 0L;

        Map.Entry<UUID, Long> oldBestTime = getBestTime();
        Map.Entry<UUID, Integer> oldMostTimes = getMostTimes();

        if (startTime != null) {
            totalTime = Calendar.getInstance().getTimeInMillis() - startTime;
            player.removeStartTime(this.getUuid());
            if (this.getLeaderboardTimes().getOrDefault(p.getUniqueId(), totalTime) >= totalTime)
                this.addLeaderboardTime(p.getUniqueId(), totalTime);
        }

        ModifiedWinMessage modifiedWinMessage = player.getWinMessage() == null ? new ModifiedWinMessage() : player.getWinMessage();
        if (startTime != null)
            modifiedWinMessage.setTime(true);
        else
            modifiedWinMessage.setTime(false);
        ChatHelper.broadcastMessage(modifiedWinMessage.toKey(), p.getName(), this.coloredName, player.getTimesCompleted(this.getUuid()), DurationFormatUtils.formatDurationHMS(totalTime));

        LevelManager.addXp(p, this);

        player.setTimesCompleted(this.getUuid(), player.getTimesCompleted(this.getUuid()) + 1);
        player.stopSong();
        player.save();

        final String wrTag = "wr_" + this.name.toLowerCase().replace(" ", "_").replaceAll("[^a-z_]", "");
        final String DELUXE_TAG = "deluxetags.tag." + wrTag;

        ArrayList<UUID> uuidSet = new ArrayList<>(LuckPermsProvider.get().getUserManager().searchAll(NodeMatcher.key(Node.builder(DELUXE_TAG).build())).join().keySet());
        uuidSet.add(p.getUniqueId());

        Map.Entry<UUID, Long> newBestTime = getBestTime();
        Map.Entry<UUID, Integer> newMostTimes = getMostTimes();

        System.out.println(oldBestTime);
        System.out.println(oldMostTimes);
        System.out.println(newBestTime);
        System.out.println(newMostTimes);

        for (UUID userUUID : uuidSet) {
            User user = LuckPermsProvider.get().getUserManager().loadUser(userUUID).join();
            boolean hasRecord = newBestTime.getKey().equals(userUUID);
            boolean hasMost = newMostTimes.getKey().equals(userUUID);
            if (hasRecord || hasMost) {
                System.out.println("Adding WR tag to " + UUIDFetcher.getName(userUUID));
                user.data().add(Node.builder(DELUXE_TAG).build());
            } else {
                System.out.println("Removing WR tag from " + UUIDFetcher.getName(userUUID));
                user.data().remove(Node.builder(DELUXE_TAG).build());
                String currTag = DeluxeTag.getPlayerTagIdentifier(userUUID.toString());
                if (currTag != null && currTag.equals(wrTag)) {
                    DeluxeTags.getPlugin(DeluxeTags.class).getDummy().setPlayerTag(userUUID.toString());
                    DeluxeTags.getPlugin(DeluxeTags.class).removeSavedTag(userUUID.toString());
                }
            }
            LuckPermsProvider.get().getUserManager().saveUser(user);
        }

        if (oldMostTimes != null && oldBestTime != null) {
            if ((p.getUniqueId().equals(newBestTime.getKey()) && oldBestTime.getValue() != newBestTime.getValue()) || (p.getUniqueId().equals(newMostTimes.getKey()) && !p.getUniqueId().equals(oldMostTimes.getKey()))) {
                ChatHelper.broadcastMessage("wr_achieved", p.getName(), this.getColoredName());
            }
        } else if (oldMostTimes == null && newMostTimes != null) {
            if (newMostTimes.getKey().equals(p.getUniqueId())) {
                ChatHelper.broadcastMessage("wr_achieved", p.getName(), this.getColoredName());
            }
        } else if (oldBestTime == null && newBestTime != null) {
            if (newBestTime.getKey().equals(p.getUniqueId())) {
                ChatHelper.broadcastMessage("wr_achieved", p.getName(), this.getColoredName());
            }
        }

        Random random = new Random();
        String shinyTag = "s_" + this.name.toLowerCase().replace(" ", "_").replaceAll("[^a-z_]", "");
        String shinyTagPerm = "deluxetags.tag." + shinyTag;
        int randInt = random.nextInt(1000);
        if (randInt < this.difficulty.toInt() && !(p.hasPermission(shinyTagPerm) && p.isPermissionSet(shinyTagPerm))) {
            User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(p);
            user.data().add(Node.builder(shinyTagPerm).build());
            LuckPermsProvider.get().getUserManager().saveUser(user);
            ChatHelper.broadcastMessage("shiny_unlocked", p.getName(), DeluxeTag.getLoadedTag(shinyTag).getDisplayTag());
        }

        Season season = Season.getSeason(this);
        if (season != null) {
            String seasonTag = "sp_" + season.getName();
            String seasonTagPerm = "deluxetags.tag." + seasonTag;
            if (!p.hasPermission(seasonTagPerm)) {
                HashMap<UUID, Integer> timesComp = player.getTimesCompleted();
                boolean hasCompletedSeason = true;
                for (Course c : season.getCourses()) {
                    if (!timesComp.containsKey(c.getUuid())) {
                        hasCompletedSeason = false;
                        break;
                    }
                }
                if (hasCompletedSeason) {
                    User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(p);
                    user.data().add(Node.builder(seasonTagPerm).build());
                    LuckPermsProvider.get().getUserManager().saveUser(user);
                    ChatHelper.broadcastMessage("tag_unlocked", p.getName(), DeluxeTag.getLoadedTag(seasonTag).getDisplayTag());
                }
            }
        }

        player.setCheckpoint(this.getUuid(), this.getStartLoc());

        this.save();

        CourseCompleteEvent courseCompleteEvent = new CourseCompleteEvent(this, p, totalTime == 0 ? Optional.empty() : Optional.of(totalTime));
        Bukkit.getPluginManager().callEvent(courseCompleteEvent);

        Bukkit.getScheduler().runTaskLater(ScovillePlugin.getInstance(), () -> {
            p.performCommand("l");
            if (!player.hasRatedCourse(this.getUuid())) {
                RateInventory rateInventory = new RateInventory(p, this.getUuid());
                rateInventory.openInventory();
            }
        }, 3L);

    }

    public String getName() {
        return name;
    }

    public MenuItem getCourseItem() {
        return menuItem;
    }

    public CourseRating getCourseRating() {
        return courseRating;
    }

    public CourseWLR getCourseWLR() {
        return courseWLR;
    }

    public void setCourseItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public void setCourseRating(CourseRating courseRating) {
        this.courseRating = courseRating;
    }

    public void setCourseWLR(CourseWLR courseWLR) {
        this.courseWLR = courseWLR;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getAuthorUUID() {
        return authorUUID;
    }

    public void setAuthorUUID(UUID authorUUID) {
        this.authorUUID = authorUUID;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public String getColoredName() {
        return coloredName;
    }

    public void setColoredName(String coloredName) {
        this.coloredName = coloredName;
    }

    public Location getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(Location startLoc) {
        this.startLoc = startLoc;
    }

    @JsonGetter("courseType")
    public CourseType getCourseType() {
        return courseType;
    }

    @JsonSetter("courseType")
    public void setCourseType(CourseType type) {
        this.courseType = type;
    }

    @JsonIgnore
    public float getWLR() {
        return this.courseWLR.getWLR();
    }

    @JsonIgnore
    public float getRating() {
        return this.courseRating.getRating();
    }

    public void addRate(int rate) {
        this.courseRating.addRate(rate);
    }

    @Override
    public String toString() {
        return "[" + this.getUuid() + "] - " +
                "Name: " + this.getName() + ", " +
                "Author: " + UUIDFetcher.getName(this.authorUUID);
    }

    public HashMap<UUID, Long> getLeaderboardTimes() {
        return leaderboardTimes;
    }

    public void addLeaderboardTime(UUID pUUID, long time) {
        this.leaderboardTimes.put(pUUID, time);
    }

    public void removeLeaderboardTime(UUID pUUID) {
        this.leaderboardTimes.remove(pUUID);
    }

    public boolean playerHasPermission(Player p) {
        String name = ScovillePlugin.PERMISSION_NAME + ".course." + this.getName().toLowerCase().replace(' ', '_');
        return (p.hasPermission(name) && p.isPermissionSet(name)) || p.isOp();
    }

    public boolean playerHasPermissions(Player p) {
        boolean b = playerHasPermission(p);
        Season s;
        if ((s = Season.getSeason(this)) != null)
            b = b || s.playerHasPermission(p);
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Course)) {
            return false;
        }

        Course c = (Course) o;

        return c.getUuid().equals(this.getUuid());
    }

}
