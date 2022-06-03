package com.uhmily.scovilleplugin.Helpers;

import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Language;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YMLHelper {

    private static final String LANGFILE = "strings";
    private static final String YMLFILE_EXT = ".yml";

    public static YamlConfiguration getLangFile(Language lang) {
        String fileName = String.format("%s-%s", LANGFILE, lang.getAbbrev());
        return getYMLConfig(fileName);
    }

    public static YamlConfiguration getYMLConfig(String name) {
        File file = new File(ScovillePlugin.getInstance().getDataFolder() + File.separator + name + YMLFILE_EXT);
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert file.exists();
        return YamlConfiguration.loadConfiguration(file);
    }

}
