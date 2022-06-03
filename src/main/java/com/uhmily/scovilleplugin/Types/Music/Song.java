package com.uhmily.scovilleplugin.Types.Music;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import com.uhmily.scovilleplugin.Helpers.FileHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Song {

    private static final ArrayList<Song> songs = new ArrayList<>();

    private final HashMap<Integer, ArrayList<Note>> notes = new HashMap<>();
    private int tick = 0;
    private int taskID = 0;
    private int finalNote = 0;
    private int loopStart = 0;
    private String author = "";
    private String name = "";
    private float volume = 1.0f;
    private short tempo = 10;
    private boolean isRadio = false;

    public Song(Song song) {
        this.notes.putAll(song.notes);
        this.tick = song.tick;
        this.finalNote = song.finalNote;
        this.loopStart = song.loopStart;
        this.author = song.author;
        this.name = song.name;
        this.volume = song.volume;
        this.tempo = song.tempo;
    }

    private Song(String songStr) {
        this(new File(FileHelper.getFolder("songs").getPath() +  File.separator + songStr + ".nbs"));
    }

    private Song(File songFile) {
        try {
            int firstCustomIndex = 16;
            FileInputStream fis = new FileInputStream(songFile);
            byte[] data = new byte[(int)songFile.length()];
            fis.read(data);
            fis.close();
            String songData = new String(data, StandardCharsets.UTF_8);
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(songFile.getPath()));
            dataInputStream.readShort();
            byte version = dataInputStream.readByte();
            byte vic = dataInputStream.readByte();
            dataInputStream.readShort();
            dataInputStream.readShort();
            this.name = readString(dataInputStream);
            this.author = readString(dataInputStream);
            readString(dataInputStream);
            readString(dataInputStream);
            this.tempo = (short) (readShort(dataInputStream) / 100);
            dataInputStream.readByte();
            dataInputStream.readByte();
            dataInputStream.readByte();
            dataInputStream.readInt();
            dataInputStream.readInt();
            dataInputStream.readInt();
            dataInputStream.readInt();
            dataInputStream.readInt();
            readString(dataInputStream);
            if (version >= 4) {
                dataInputStream.readByte();
                this.loopStart = dataInputStream.readByte();
                if (!songData.contains("format4beta"))
                    dataInputStream.readShort();
            }
            int ca = -1;
            while (true) {
                short a = readShort(dataInputStream);
                if (a == 0)
                    break;
                ca += a;
                while (true) {
                    short pitch = 0;
                    int vel;
                    a = readShort(dataInputStream);
                    if (a == 0)
                        break;
                    byte ins = dataInputStream.readByte();
                    if (ins >= vic)
                        ins = (byte)(ins + firstCustomIndex - vic);
                    byte key = dataInputStream.readByte();
                    if (version >= 4) {
                        vel = dataInputStream.readUnsignedByte();
                        dataInputStream.readByte();
                        pitch = readShort(dataInputStream);
                    } else {
                        vel = 100;
                    }
                    Note note = new Note(ca, key, ins, vel, pitch);
                    ArrayList<Note> anotes = this.notes.getOrDefault(note.getTick(), new ArrayList<>());
                    anotes.add(note);
                    this.notes.put(note.getTick(), anotes);
                    this.finalNote = Math.max(note.getTick(), this.finalNote);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(Player p) {
        this.taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(ScovillePlugin.getInstance(), () -> {
            ArrayList<Note> anotes = this.notes.getOrDefault(this.tick, new ArrayList<>());
            anotes.stream().map(Note::new).peek(note -> note.setVolume(note.getVolume() * this.volume)).forEach(note -> note.play(p));
            this.tick = (this.tick + 1) % (this.finalNote + 1);
        }, 0L, 20 / this.tempo);
    }

    public void runOnce(Player p) {
        this.taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(ScovillePlugin.getInstance(), () -> {
            ArrayList<Note> anotes = this.notes.getOrDefault(this.tick, new ArrayList<>());
            anotes.stream().map(Note::new).peek(note -> note.setVolume(note.getVolume() * this.volume)).forEach(note -> note.play(p));
            this.tick++;
        }, 0L, 20 / this.tempo);
    }

    public void cancel() {
        pause();
        this.tick = 0;
    }

    public void fadeOut(Player p) {
        Bukkit.getScheduler().cancelTask(this.taskID);
        final float decrease = this.volume * 0.1f;
        this.taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(ScovillePlugin.getInstance(), () -> {
            if (this.volume < 0.1f) {
                Bukkit.getScheduler().cancelTask(this.taskID);
            }
            ArrayList<Note> anotes = this.notes.getOrDefault(this.tick, new ArrayList<>());
            anotes.stream().map(Note::new).peek(note -> note.setVolume(note.getVolume() * this.volume)).forEach(note -> note.play(p));
            this.volume -= decrease;
            this.tick = (this.tick + 1) % (this.finalNote + 1);
        }, 0L, 20 / this.tempo);
    }

    public void fadeIn(Player p, float finalVolume) {
        this.volume = 0.0f;
        final float increase = finalVolume * 0.1f;
        this.taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(ScovillePlugin.getInstance(), () -> {
            if (Math.abs(this.volume - finalVolume) < 0.1f) {
                Bukkit.getScheduler().cancelTask(this.taskID);
                this.run(p);
            }
            ArrayList<Note> anotes = this.notes.getOrDefault(this.tick, new ArrayList<>());
            anotes.stream().map(Note::new).peek(note -> note.setVolume(note.getVolume() * this.volume)).forEach(note -> note.play(p));
            this.volume += increase;
            this.tick = (this.tick + 1) % (this.finalNote + 1);
        }, 0L, 20 / this.tempo);
    }

    public void pause() {
        Bukkit.getScheduler().cancelTask(this.taskID);
    }

    public static short readShort(DataInputStream dataInputStream) throws IOException {
        int byte1 = dataInputStream.readUnsignedByte();
        int byte2 = dataInputStream.readUnsignedByte();
        return (short)(byte1 + (byte2 << 8));
    }

    public static int readInt(DataInputStream dataInputStream) throws IOException {
        int byte1 = dataInputStream.readUnsignedByte();
        int byte2 = dataInputStream.readUnsignedByte();
        int byte3 = dataInputStream.readUnsignedByte();
        int byte4 = dataInputStream.readUnsignedByte();
        return byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24);
    }

    public static String readString(DataInputStream dataInputStream) throws IOException {
        int length = readInt(dataInputStream);
        StringBuilder builder = new StringBuilder(length);
        for (; length > 0; length--) {
            char c = (char)dataInputStream.readByte();
            if (c == '\r')
                c = ' ';
            builder.append(c);
        }
        return builder.toString();
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public void save() {
        songs.removeIf(song -> song.name.equalsIgnoreCase(this.name));
        songs.add(this);
    }

    public static void loadSongs() {
        File songFolder = FileHelper.getFolder("songs");
        for (File song : songFolder.listFiles()) {
            new Song(song).save();
        }
    }

    public static Song getSong(String name) {
        return new Song(songs.stream().filter(song -> song.getName().equalsIgnoreCase(name)).findFirst().get());
    }

    public static boolean hasSong(String name) {
        return songs.stream().anyMatch(song -> song.getName().equalsIgnoreCase(name));
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public static ArrayList<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    public boolean isRadio() {
        return isRadio;
    }

    public void setIsRadio(boolean isRadio) {
        this.isRadio = isRadio;
    }

}