package com.uhmily.scovilleplugin.Helpers;

import com.uhmily.scovilleplugin.Types.Language;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChatHelper {

    /**
     * Replaces & color codes in text with their respective colors in-game.
     *
     * @param s The string with & color codes. (e.g. '&eHello World!')
     * @return The string with colors instead of the codes.
     */
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Finds the string with the given key in a translation file and formats it with the given args.
     *
     * @param key The key for the string to use.
     * @param lang The language to get the string for.
     * @param args The arguments for the string.
     * @return The formatted string.
     */
    public static String format(String key, Language lang, Object... args) {
        YamlConfiguration langConfig = YMLHelper.getLangFile(lang);
        if (!langConfig.contains(key)) {
            return "";
        }
        MessageFormat fmt = new MessageFormat(langConfig.getString(key));
        return ChatHelper.color(fmt.format(args));
    }

    /**
     * Finds the string with the given key in a translation file and formats it with the given args.
     *
     * @param key The key for the string to use.
     * @param sp The player to get the string for.
     * @param args The arguments for the string.
     * @return The formatted string.
     */
    public static String format(String key, ScovillePlayer sp, Object... args) {
        return format(key, sp.getLang(), args);
    }

    /**
     * Finds the string with the given key in a translation file and formats it with the given args.
     *
     * @param key The key for the string to use.
     * @param p The player to get the string for.
     * @param args The arguments for the string.
     * @return The formatted string.
     */
    public static String format(String key, Player p, Object... args) {
        return format(key, ScovillePlayer.getPlayer(p), args);
    }

    /**
     * Finds the string with the given key in a translation file and formats it with the given args.
     *
     * @param key The key for the string to use.
     * @param p The player to get the string for.
     * @param args The arguments for the string.
     * @return The formatted string.
     */
    public static String format(String key, UUID p, String... args) {
        return format(key, Objects.requireNonNull(ScovillePlayer.getPlayer(p)), args);
    }

    public static String[] formatArray(String key, Player p, Object... args) {
        return formatArray(key, Objects.requireNonNull(ScovillePlayer.getPlayer(p.getUniqueId())), args);
    }

    public static String[] formatArray(String key, UUID p, Object... args) {
        return formatArray(key, Objects.requireNonNull(ScovillePlayer.getPlayer(p)), args);
    }

    public static String[] formatArray(String key, ScovillePlayer sp, Object... args) {
        return formatArray(key, sp.getLang(), args);
    }

    public static String[] formatArray(String key, Language lang, Object... args) {
        YamlConfiguration langConfig = YMLHelper.getLangFile(lang);
        if (!langConfig.contains(key)) {
            return new String[0];
        }
        List<String> lines = langConfig.getStringList(key);
        List<String> formattedLines = new ArrayList<>();
        for (String line : lines) {
            String str = ChatHelper.color(new MessageFormat(line).format(args));
            formattedLines.add(str);
        }
        return formattedLines.toArray(new String[0]);
    }

    @NotNull
    public static String reverseToAltColor(@NotNull String textToReverse, char altChar) {
        char[] chars = textToReverse.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ChatColor.COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(chars[i + 1]) > -1) {
                chars[i] = altChar;
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }

        return new String(chars);
    }

    @NotNull
    public static ChatColor charToColor(char ch) {
        switch (ch) {
            case '0': return ChatColor.BLACK;
            case '1': return ChatColor.DARK_BLUE;
            case '2': return ChatColor.DARK_GREEN;
            case '3': return ChatColor.DARK_AQUA;
            case '4': return ChatColor.DARK_RED;
            case '5': return ChatColor.DARK_PURPLE;
            case '6': return ChatColor.GOLD;
            case '7': return ChatColor.GRAY;
            case '8': return ChatColor.DARK_GRAY;
            case '9': return ChatColor.BLUE;
            case 'a': return ChatColor.GREEN;
            case 'b': return ChatColor.AQUA;
            case 'c': return ChatColor.RED;
            case 'd': return ChatColor.LIGHT_PURPLE;
            case 'e': return ChatColor.YELLOW;
            case 'f': return ChatColor.WHITE;
            default: return ChatColor.RESET;
        }
    }

    public static void broadcastMessage(String tag, Object... args) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatHelper.format(tag, p, args));
        }
    }

}
