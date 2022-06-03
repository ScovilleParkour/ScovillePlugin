package com.uhmily.scovilleplugin.Types.Plate;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Json.Deserializers.LocationDeserializer;
import com.uhmily.scovilleplugin.Types.Json.Serializers.LocationSerializer;
import com.uhmily.scovilleplugin.Types.ScovilleObject;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class Plate extends ScovilleObject {

    private static final ArrayList<Plate> plates = new ArrayList<>();

    @JsonSerialize(using = LocationSerializer.class)
    @JsonDeserialize(using = LocationDeserializer.class)
    private final Location plateLoc;
    private UUID course;

    private Plate(Location loc) {
        super();
        this.plateLoc = loc;
        this.course = null;
        plates.add(this);
    }

    public static ArrayList<Plate> getPlates() {
        return new ArrayList<>(plates);
    }

    public static boolean plateExists(Location loc) {
        return plates.stream().anyMatch(plate -> plate.getPlateLoc().equals(loc));
    }

    @JsonCreator
    public static Plate getPlate(@JsonProperty("plateLoc") Location loc) {
        Optional<Plate> pl = plates.stream().filter(plate -> plate.getPlateLoc().equals(loc)).findFirst();
        if (pl.isPresent()) {
            return pl.get();
        }
        return new Plate(loc);
    }

    public void save() {
        plates.removeIf(plate -> plate.getUuid().equals(this.getUuid()));
        plates.add(this);
    }

    public Location getPlateLoc() {
        return plateLoc;
    }

    @JsonIgnore
    public Course getCourse() {
        return Course.getCourse(this.course);
    }

    @JsonGetter("course")
    public UUID getCourseUUID() {
        return this.course;
    }

    @JsonSetter("course")
    public void setCourseUUID(UUID course) {
        this.course = course;
    }

}
