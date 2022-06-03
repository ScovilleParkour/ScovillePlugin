package com.uhmily.scovilleplugin.Types.Tag;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uhmily.scovilleplugin.Helpers.JsonHelper;
import me.clip.deluxetags.DeluxeTag;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TagManager {

    private final static TagManager instance = new TagManager();

    private final static HashMap<TagType, ArrayList<String>> tags = new HashMap<>();

    private TagManager() {}

    public static TagManager getInstance() {
        return instance;
    }

    public static void load() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        TypeReference<HashMap<TagType, ArrayList<String>>> typeRef = new TypeReference<HashMap<TagType, ArrayList<String>>>() {};
        try {
            tags.putAll(mapper.readValue(JsonHelper.getJsonFile("tags"), typeRef));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void save() {

        ObjectMapper mapper = new ObjectMapper();

        try {
            FileWriter writer = new FileWriter(JsonHelper.getJsonFile("tags"));
            writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tags));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static HashMap<TagType, ArrayList<String>> getTags() {
        return tags;
    }

    public static ArrayList<String> getTags(TagType type) { return tags.computeIfAbsent(type, k -> new ArrayList<>()); }

    public static void addTag(TagType type, DeluxeTag tag) {
        ArrayList<String> typedTags = tags.computeIfAbsent(type, k -> new ArrayList<>());
        typedTags.add(tag.getIdentifier());
    }

    public static void removeTag(DeluxeTag tag) {
        tags.values().forEach(list -> list.remove(tag.getIdentifier()));
    }

}
