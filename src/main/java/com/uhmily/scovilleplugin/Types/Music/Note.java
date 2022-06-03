package com.uhmily.scovilleplugin.Types.Music;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Note {
    private int tick;

    private Sound sound;

    private float volume;

    private float pitch;

    public Note(Note note) {
        this.tick = note.tick;
        this.sound = note.sound;
        this.volume = note.volume;
        this.pitch = note.pitch;
    }

    public Note(int tick, int note, int ins, int vel, short pit) {
        this.tick = tick;
        this.pitch = (float)Math.pow(2.0D, ((note + (pit / 100.0F) - 45.0F) / 12.0F));
        Sound[] sounds = new Sound[16];
        sounds[0] = Sound.BLOCK_NOTE_HARP;
        sounds[1] = Sound.BLOCK_NOTE_BASS;
        sounds[2] = Sound.BLOCK_NOTE_BASEDRUM;
        sounds[3] = Sound.BLOCK_NOTE_SNARE;
        sounds[4] = Sound.BLOCK_NOTE_HAT;
        sounds[5] = Sound.BLOCK_NOTE_GUITAR;
        sounds[6] = Sound.BLOCK_NOTE_FLUTE;
        sounds[7] = Sound.BLOCK_NOTE_BELL;
        sounds[8] = Sound.BLOCK_NOTE_CHIME;
        sounds[9] = Sound.BLOCK_NOTE_XYLOPHONE;
        sounds[10] = null;
        sounds[11] = null;
        sounds[12] = null;
        sounds[13] = null;
        sounds[14] = null;
        sounds[15] = Sound.BLOCK_NOTE_PLING;
        if (sounds[ins] == null) System.out.println(ins);
        this.sound = sounds[ins];
        this.volume = vel / 100.0F;
    }

    public void play(Player p) {
        try {
            p.playSound(p.getLocation(), this.sound, SoundCategory.RECORDS, this.volume, this.pitch);
        } catch (NullPointerException e) {
            System.out.println("Location: " + p.getLocation() + " | Sound: " + this.sound + " | Vol: " + this.volume + " | Pitch: " + this.pitch);
            e.printStackTrace();
        }
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getVolume() {
        return this.volume;
    }

    public int getTick() {
        return this.tick;
    }

    public Sound getSound() {
        return this.sound;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public String toString() {
        return "Tick: " + this.tick + " Sound: " + this.sound + " Vol: " + this.volume + " Pitch: " + this.pitch;
    }
}
