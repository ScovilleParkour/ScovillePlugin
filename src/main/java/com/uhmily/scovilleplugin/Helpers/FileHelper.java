package com.uhmily.scovilleplugin.Helpers;

import com.uhmily.scovilleplugin.ScovillePlugin;

import java.io.File;

public class FileHelper {

    public static File getFolder(String folderName) {
        File folder = new File(ScovillePlugin.getInstance().getDataFolder() + File.separator + folderName);
        if (!folder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            folder.mkdir();
        }
        assert folder.exists();
        return folder;
    }

}
