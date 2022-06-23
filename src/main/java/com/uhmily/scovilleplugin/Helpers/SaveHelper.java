package com.uhmily.scovilleplugin.Helpers;

import com.uhmily.scovilleplugin.ScovillePlugin;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SaveHelper {

    private final static String BACKUP_FOLDER = ScovillePlugin.getInstance().getDataFolder().getPath() + File.separator + "backup";
    private final static String BACKUP_FILE = "{0}.zip";

    public static File getBackupFolder() {
        File folder = new File(BACKUP_FOLDER);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdir();
        }
        return folder;
    }

    public static void makeBackup() {
        Iterator<File> fileIterator = FileUtils.iterateFiles(ScovillePlugin.getInstance().getDataFolder(), new String[]{ "json" }, false);
        MessageFormat messageFormat = new MessageFormat(getBackupFolder() + File.separator + BACKUP_FILE);
        zipFiles(messageFormat.format(new Object[]{Instant.now().toString().replace("-", "").replace(":", "")}), fileIterator);
        purgeBackups(new File(BACKUP_FOLDER));
    }

    public static void zipFiles(String name, Iterator<File> files) {
        try {
            FileOutputStream fos = new FileOutputStream(name);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            while (files.hasNext()) {
                File fileToZip = files.next();
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
        } catch (IOException e) {
            Bukkit.getLogger().warning("Could not backup data!");
            e.printStackTrace();
        }
    }

    public static void purgeBackups(File backupLoc) {
        File[] backupFiles = backupLoc.listFiles();
        long oldestDate = Long.MAX_VALUE;
        File oldestFile = null;
        if (backupFiles != null && backupFiles.length > 10) {
            for (File f : backupFiles) {
                if (!f.isDirectory() && f.lastModified() < oldestDate) {
                    oldestDate = f.lastModified();
                    oldestFile = f;
                }
            }
        }
        if (oldestFile != null) {
            oldestFile.delete();
        }
    }

}
