package com.uhmily.scovilleplugin.Types.TPSigns;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uhmily.scovilleplugin.Types.Json.Deserializers.LocationDeserializer;
import com.uhmily.scovilleplugin.Types.Json.Serializers.LocationSerializer;
import com.uhmily.scovilleplugin.Types.ScovilleObject;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Optional;

public class TPSign extends ScovilleObject {

    private static final ArrayList<TPSign> tpSigns = new ArrayList<>();

    @JsonSerialize(using = LocationSerializer.class)
    @JsonDeserialize(using = LocationDeserializer.class)
    private final Location tpSignLoc;
    @JsonSerialize(using = LocationSerializer.class)
    @JsonDeserialize(using = LocationDeserializer.class)
    private Location tpLoc;

    private TPSign(Location loc) {
        super();
        this.tpSignLoc = loc;
        tpSigns.add(this);
    }

    public static ArrayList<TPSign> getTpSigns() {
        return new ArrayList<>(tpSigns);
    }

    public static boolean signExists(Location loc) {
        return tpSigns.stream().anyMatch(sign -> sign.getTpSignLoc().equals(loc));
    }

    @JsonCreator
    public static TPSign getSign(@JsonProperty("tpSignLoc") Location loc) {
        Optional<TPSign> s = tpSigns.stream().filter(sign -> sign.getTpSignLoc().equals(loc)).findFirst();
        if (s.isPresent()) {
            return s.get();
        }
        return new TPSign(loc);
    }

    public void save() {
        tpSigns.removeIf(sign -> sign.getUuid().equals(this.getUuid()));
        tpSigns.add(this);
    }

    public Location getTpSignLoc() {
        return tpSignLoc;
    }

    public void setTpLoc(Location tpLoc) {
        this.tpLoc = tpLoc;
    }

    public Location getTpLoc() {
        return tpLoc;
    }
}