package com.uhmily.scovilleplugin.Helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.uhmily.scovilleplugin.Achievements.AchievementManager;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Hotbar.Hotbar;
import com.uhmily.scovilleplugin.Types.Plate.Plate;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.ScovilleObject;
import com.uhmily.scovilleplugin.Types.Season.Season;
import com.uhmily.scovilleplugin.Types.TPSigns.TPSign;
import com.uhmily.scovilleplugin.Types.Tag.TagManager;
import com.uhmily.scovilleplugin.Types.TagSign.TagSign;

import java.io.*;
import java.util.List;

public class JsonHelper {

    public static File getJsonFile(String jsonFile) {
        String fileName = jsonFile + ".json";
        File file = new File(ScovillePlugin.getInstance().getDataFolder() + File.separator + fileName);
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert file.exists();
        return file;
    }

    public static <T> void saveItems(List<T> items, File jsonFile) {
        ObjectMapper mapper = new ObjectMapper();
        String data = "[]";
        try {
            data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(items);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(jsonFile);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static <T extends ScovilleObject> void loadItems(List<T> items, File jsonFile) {

        if (jsonFile.length() == 0) return;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            ArrayNode arrayNode = (ArrayNode) mapper.readTree(jsonFile);
            if (!arrayNode.isArray()) return;
            items.clear();
            for (JsonNode node : arrayNode) {
                Class<? extends ScovilleObject> clazz = (Class<? extends ScovilleObject>)Class.forName(node.get("type").asText());
                items.add((T)mapper.convertValue(node, clazz));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveData() {
        new Thread(() -> {
            JsonHelper.saveItems(Course.getCourses(), JsonHelper.getJsonFile("courses"));
            // JsonHelper.saveItems(RankupCourse.getCourses(), JsonHelper.getJsonFile("rankups"));
            JsonHelper.saveItems(ScovillePlayer.getPlayers(), JsonHelper.getJsonFile("players"));
            JsonHelper.saveItems(Season.getSeasons(), JsonHelper.getJsonFile("seasons"));
            JsonHelper.saveItems(Plate.getPlates(), JsonHelper.getJsonFile("plates"));
            JsonHelper.saveItems(TPSign.getTpSigns(), JsonHelper.getJsonFile("tpsigns"));
            JsonHelper.saveItems(AchievementManager.getAchievements(), JsonHelper.getJsonFile("achievements"));
            JsonHelper.saveItems(Hotbar.getHotbars(), JsonHelper.getJsonFile("hotbars"));
            JsonHelper.saveItems(TagSign.getTpSigns(), JsonHelper.getJsonFile("tagsigns"));
            TagManager.save();
        }).start();
    }

    public static void loadData() {
        new Thread(() -> {
            JsonHelper.loadItems(Course.getCourses(), JsonHelper.getJsonFile("courses"));
            // JsonHelper.saveItems(RankupCourse.getCourses(), JsonHelper.getJsonFile("rankups"));
            JsonHelper.loadItems(ScovillePlayer.getPlayers(), JsonHelper.getJsonFile("players"));
            JsonHelper.loadItems(Season.getSeasons(), JsonHelper.getJsonFile("seasons"));
            JsonHelper.loadItems(Plate.getPlates(), JsonHelper.getJsonFile("plates"));
            JsonHelper.loadItems(TPSign.getTpSigns(), JsonHelper.getJsonFile("tpsigns"));
            JsonHelper.loadItems(AchievementManager.getAchievements(), JsonHelper.getJsonFile("achievements"));
            JsonHelper.loadItems(Hotbar.getHotbars(), JsonHelper.getJsonFile("hotbars"));
            JsonHelper.loadItems(TagSign.getTpSigns(), JsonHelper.getJsonFile("tagsigns"));
            TagManager.load();
        }).start();
    }

}
