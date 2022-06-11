package com.uhmily.scovilleplugin.Types.Player;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.JoinLeaveMessage;
import com.uhmily.scovilleplugin.Types.Json.Deserializers.LocationDeserializer;
import com.uhmily.scovilleplugin.Types.Json.Serializers.LocationSerializer;
import com.uhmily.scovilleplugin.Types.Language;
import com.uhmily.scovilleplugin.Types.Music.Song;
import com.uhmily.scovilleplugin.Types.Rank;
import com.uhmily.scovilleplugin.Types.ScovilleObject;
import com.uhmily.scovilleplugin.Types.WinMessage.ModifiedWinMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ScovillePlayer extends ScovilleObject {

    private static final List<ScovillePlayer> players = new ArrayList<>();

    @JsonProperty("timesCompleted")
    private final HashMap<UUID, Integer> timesCompleted;
    @JsonSetter(nulls = Nulls.SKIP)
    private Rank rank = Rank.BELL;
    @JsonSetter(nulls = Nulls.SKIP)
    private Language lang = Language.ENGLISH;
    private final HashMap<UUID, Long> startTimes;
    @JsonSerialize(contentUsing = LocationSerializer.class)
    @JsonDeserialize(contentUsing = LocationDeserializer.class)
    private final HashMap<UUID, Location> checkpoints;
    private UUID currentlyPlaying;
    @JsonSetter(nulls = Nulls.SKIP)
    private boolean toggleChat = true;
    @JsonSetter(nulls = Nulls.SKIP)
    private boolean playerVis = true;
    @JsonSetter(nulls = Nulls.SKIP)
    private boolean joinPlayer = true;
    @JsonSetter(nulls = Nulls.SKIP)
    private boolean music = true;
    @JsonIgnore
    private Song currentSong;
    private final Set<UUID> coursesRated;
    private final ArrayList<String> achievements;
    private boolean isPracticing;
    @JsonSerialize(using = LocationSerializer.class)
    @JsonDeserialize(using = LocationDeserializer.class)
    private Location practiceCP;
    @JsonSerialize(using = LocationSerializer.class)
    @JsonDeserialize(using = LocationDeserializer.class)
    private Location practiceStart;
    private String tagIdentifier;
    private ChatColor chatColor;
    private UUID hotBar;
    private JoinLeaveMessage joinLeaveMessage;
    private ModifiedWinMessage winMessage;
    private long xp;
    private final Set<UUID> played;
    @JsonSetter(nulls = Nulls.SKIP)
    private boolean isStartAtCP = true;

    private ScovillePlayer(UUID p) {
        this(p, new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashSet<>(), new ArrayList<>(), new HashSet<>());
    }

    private ScovillePlayer(UUID p, HashMap<UUID, Integer> tC, HashMap<UUID, Long> sT, HashMap<UUID, Location> cp, Set<UUID> cR, ArrayList<String> a, Set<UUID> pl) {
        super(p);
        this.timesCompleted = tC;
        this.startTimes = sT;
        this.checkpoints = cp;
        this.coursesRated = cR;
        this.achievements = a;
        this.played = pl;
        players.add(this);
    }

    @Nullable
    public static ScovillePlayer getPlayer(Player p) {
        return getPlayer(p.getUniqueId());
    }

    @Nullable
    public static ScovillePlayer getPlayer(UUID uuid) {
        if (uuid == null) return null;
        Optional<ScovillePlayer> p = players.stream().filter(scovillePlayer -> scovillePlayer.getUuid().equals(uuid)).findFirst();
        if (p.isPresent()) {
            return p.get();
        }
        return new ScovillePlayer(uuid);
    }

    @JsonCreator
    @Nullable
    public static ScovillePlayer getPlayer(@JsonProperty("uuid") UUID uuid, @JsonProperty("timesCompleted") HashMap<UUID, Integer> timesCompleted, @JsonProperty("startTimes") HashMap<UUID, Long> startTimes, @JsonProperty("checkpoints") @JsonDeserialize(contentUsing = LocationDeserializer.class) HashMap<UUID, Location> checkpoints, @JsonProperty("coursesRated") Set<UUID> coursesRated, @JsonProperty("achievements") ArrayList<String> achievements, @JsonProperty("played") Set<UUID> played) {
        if (uuid == null) return null;
        if (timesCompleted == null) { timesCompleted = new HashMap<>(); }
        if (startTimes == null) { startTimes = new HashMap<>(); }
        if (checkpoints == null)  { checkpoints = new HashMap<>(); }
        if (coursesRated == null)  { coursesRated = new HashSet<>(); }
        if (achievements == null)  { achievements = new ArrayList<>(); }
        if (played == null) { played = new HashSet<>(); }

        Optional<ScovillePlayer> p = players.stream().filter(scovillePlayer -> scovillePlayer.getUuid().equals(uuid)).findFirst();
        if (!p.isPresent()) {
            return new ScovillePlayer(uuid, timesCompleted, startTimes, checkpoints, coursesRated, achievements, played);
        }
        ScovillePlayer sp = p.get();
        sp.timesCompleted.putAll(timesCompleted);
        sp.startTimes.putAll(startTimes);
        sp.checkpoints.putAll(checkpoints);
        sp.coursesRated.addAll(coursesRated);
        sp.achievements.addAll(achievements);
        sp.played.addAll(played);
        return sp;
    }

    public static boolean hasPlayer(UUID uuid) {
        return players.stream().anyMatch(scovillePlayer -> scovillePlayer.getUuid().equals(uuid));
    }

    @NotNull
    public static List<ScovillePlayer> getPlayers() {
        return new ArrayList<>(players);
    }

    public void save() {
        players.removeIf(scovillePlayer -> scovillePlayer.getUuid().equals(this.getUuid()));
        players.add(this);
    }

    @NotNull
    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @NotNull
    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    @NotNull
    public HashMap<UUID, Integer> getTimesCompleted() {
        return timesCompleted;
    }

    public int getTimesCompleted(UUID cUUID) {
        if (this.timesCompleted.containsKey(cUUID)) {
            return this.timesCompleted.get(cUUID);
        }
        return 0;
    }

    public void setTimesCompleted(UUID cUUID, int times) {
        this.timesCompleted.put(cUUID, times);
    }

    @NotNull
    public HashMap<UUID, Long> getStartTimes() {
        return startTimes;
    }

    public Long getStartTime(UUID cUUID) {
        if (this.startTimes.containsKey(cUUID)) {
            return this.startTimes.get(cUUID);
        }
        return null;
    }

    public void addStartTime(UUID cUUID, long time) {
        this.startTimes.put(cUUID, time);
    }

    public void removeStartTime(UUID cUUID) {
        this.startTimes.remove(cUUID);
    }

    @NotNull
    public HashMap<UUID, Location> getCheckpoints() {
        return checkpoints;
    }

    @Nullable
    public Location getCheckpoint(UUID cUUID) {
        return this.checkpoints.get(cUUID);
    }

    public void setCheckpoint(UUID cUUID, Location loc) {
        this.checkpoints.put(cUUID, loc);
    }

    public void checkpoint(UUID cUUID) {
        Location checkpoint = this.getCheckpoint(cUUID);
        if (checkpoint == null) { Bukkit.getPlayer(this.getUuid()).teleport(Course.getCourse(cUUID).getStartLoc()); }
        else { Bukkit.getPlayer(this.getUuid()).teleport(this.getCheckpoint(cUUID)); }
    }

    public void setCurrentlyPlaying(UUID currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
    }

    @Nullable
    public UUID getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public boolean isToggleChat() {
        return toggleChat;
    }

    public void setToggleChat(boolean toggleChat) {
        this.toggleChat = toggleChat;
    }

    public void toggleChat() {
        this.toggleChat = !this.toggleChat;
    }

    public void setPlayerVis(boolean playerVis) {
        this.playerVis = playerVis;
    }

    public boolean isPlayerVis() {
        return playerVis;
    }

    public void togglePlayerVis() { this.playerVis = !this.playerVis; }

    public void setJoinPlayer(boolean joinPlayer) {
        this.joinPlayer = joinPlayer;
    }

    public boolean isJoinPlayer() {
        return joinPlayer;
    }

    public void toggleJoinPlayer() { this.joinPlayer = !this.joinPlayer; }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isMusic() {
        return music;
    }

    public void toggleMusic() { this.music = !this.music; }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    public void playSong(Song song) {
        if (this.currentSong == song) return;
        Player p = Bukkit.getPlayer(this.getUuid());
        this.stopSong();
        song.run(p);
        this.setCurrentSong(song);
    }

    public void fadeSong(Song song) {
        if (this.currentSong == song) return;
        Player p = Bukkit.getPlayer(this.getUuid());
        this.currentSong.fadeOut(p);
        this.currentSong = song;
        this.currentSong.fadeIn(p, 1.0f);
    }

    public void stopSong() {
        if (this.getCurrentSong() == null) return;
        this.currentSong.cancel();
        this.currentSong = null;
    }

    public Set<UUID> getCoursesRated() {
        return coursesRated;
    }

    public void addRatedCourse(UUID cUUID) {
        this.coursesRated.add(cUUID);
    }

    public boolean hasRatedCourse(UUID cUUID) {
        return this.coursesRated.contains(cUUID);
    }

    public <T extends Achievement<?>> boolean hasAchievement(T achievement) {
        return this.achievements.contains(achievement.getClass().getName());
    }

    public <T extends Achievement<?>> void unlockAchievement(T achievement) {
        if (!this.isOnline()) return;
        this.achievements.add(achievement.getClass().getName());
        Player p = Bukkit.getPlayer(this.getUuid());
        NamespacedKey key = new NamespacedKey("scoville", achievement.getAchievement());
        AdvancementProgress progress = p.getAdvancementProgress(Bukkit.getAdvancement(key));
        for (String criteria : progress.getRemainingCriteria())
            progress.awardCriteria(criteria);
        if (achievement.getType().equals(AchievementDiff.SPECIAL)) {
            NamespacedKey specialKey = NamespacedKey.minecraft("scoville:special");
            AdvancementProgress progress1 = p.getAdvancementProgress(Bukkit.getAdvancement(specialKey));
            for (String criteria : progress1.getRemainingCriteria())
                progress1.awardCriteria(criteria);
        }
        this.xp += achievement.getAchievementDiff().getXp();
    }

    public ArrayList<String> getAchievements() {
        return achievements;
    }

    public void setPracticeCP(Location practiceCP) {
        this.practiceCP = practiceCP;
    }

    public Location getPracticeCP() {
        return practiceCP;
    }

    public void setPracticing(boolean practicing) {
        isPracticing = practicing;
    }

    public boolean isPracticing() {
        return isPracticing;
    }

    public void togglePracticing() {
        this.isPracticing = !this.isPracticing;
    }

    public String getTagIdentifier() {
        return tagIdentifier;
    }

    public void setTagIdentifier(String tagIdentifier) {
        this.tagIdentifier = tagIdentifier;
    }

    public ChatColor getChatColor() {
        if (chatColor == null)
            return ChatColor.WHITE;
        return chatColor;
    }

    public void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public UUID getHotBar() {
        return hotBar;
    }

    public void setHotBar(UUID hotBar) {
        this.hotBar = hotBar;
    }

    public JoinLeaveMessage getMessage() {
        return joinLeaveMessage;
    }

    public void setMessage(JoinLeaveMessage message) {
        this.joinLeaveMessage = message;
    }

    public ModifiedWinMessage getWinMessage() {
        return winMessage;
    }

    public void setWinMessage(ModifiedWinMessage winMessage) {
        this.winMessage = winMessage;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public void grantXp(long xp) {
        this.xp += xp;
    }

    public void setPracticeStart(Location practiceStart) {
        this.practiceStart = practiceStart;
    }

    public Location getPracticeStart() {
        return practiceStart;
    }

    public Set<UUID> getPlayed() {
        return played;
    }

    public boolean hasPlayed(UUID cUUID) {
        return this.played.contains(cUUID);
    }

    public void addPlayed(UUID cUUID) {
        this.played.add(cUUID);
    }

    @JsonIgnore
    public boolean isOnline() {
        return Bukkit.getPlayer(this.getUuid()) != null;
    }

    public boolean isStartAtCP() {
        return isStartAtCP;
    }

    public void setStartAtCP(boolean startAtCP) {
        isStartAtCP = startAtCP;
    }

    public void toggleStartAtCP() {
        this.isStartAtCP = !this.isStartAtCP;
    }

    @Override
    public String toString() {
        return "[" + this.getUuid() + "] - " +
                "Name: " + UUIDFetcher.getName(this.getUuid()) + " | " +
                "Rank: " + this.getRank() + " | " +
                "Lang: " + this.getLang().getAbbrev();
    }

}
