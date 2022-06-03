package com.uhmily.scovilleplugin.Types.TagSign;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uhmily.scovilleplugin.Types.Json.Deserializers.LocationDeserializer;
import com.uhmily.scovilleplugin.Types.Json.Serializers.LocationSerializer;
import com.uhmily.scovilleplugin.Types.ScovilleObject;
import com.uhmily.scovilleplugin.Types.TPSigns.TPSign;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Optional;

public class TagSign extends ScovilleObject {

    private static final ArrayList<TagSign> tagSigns = new ArrayList<>();

    @JsonSerialize(using = LocationSerializer.class)
    @JsonDeserialize(using = LocationDeserializer.class)
    private final Location tagSignLoc;
    private String tagID;

    private TagSign(Location loc) {
        super();
        this.tagSignLoc = loc;
        tagSigns.add(this);
    }

    public static ArrayList<TagSign> getTpSigns() {
        return new ArrayList<>(tagSigns);
    }

    public static boolean signExists(Location loc) {
        return tagSigns.stream().anyMatch(sign -> sign.getTagSignLoc().equals(loc));
    }

    @JsonCreator
    public static TagSign getSign(@JsonProperty("tagSignLoc") Location loc) {
        Optional<TagSign> s = tagSigns.stream().filter(sign -> sign.getTagSignLoc().equals(loc)).findFirst();
        if (s.isPresent()) {
            return s.get();
        }
        return new TagSign(loc);
    }

    public void save() {
        tagSigns.removeIf(sign -> sign.getUuid().equals(this.getUuid()));
        tagSigns.add(this);
    }

    public Location getTagSignLoc() {
        return tagSignLoc;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public String getTagID() {
        return tagID;
    }

}
