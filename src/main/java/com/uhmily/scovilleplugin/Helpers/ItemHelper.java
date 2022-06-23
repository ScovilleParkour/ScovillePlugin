package com.uhmily.scovilleplugin.Helpers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.uhmily.scovilleplugin.Types.Language;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ItemHelper {

    /**
     * Creates an item with the given traits.
     *
     * @param title The title of the item.
     * @param mat The material of the item.
     * @param dam The damage value (e.g. 0 for Stone, 1 for Granite, etc.)
     * @param lore The lore of the item.
     * @return The ItemStack with the given traits.
     */
    public static ItemStack createItem(String title, Material mat, short dam, String... lore) {

        ItemStack item = new ItemStack(mat, 1, dam);
        ItemMeta iMeta = item.getItemMeta();
        assert iMeta != null;
        iMeta.setDisplayName(ChatHelper.color(title));
        iMeta.setLore(Arrays.stream(lore)
                .map(ChatHelper::color)
                .collect(Collectors.toList()));
        item.setItemMeta(iMeta);
        return item;
    }

    public static ItemStack createItem(String title, Material mat, String... lore) {
        return createItem(title, mat, (short)0, lore);
    }

    public static ItemStack createItem(String title, Player p, String... lore) {
        return createItem(title, p.getUniqueId(), lore);
    }

    public static ItemStack createItem(String title, UUID uuid, String... lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta sMeta = (SkullMeta) item.getItemMeta();
        assert sMeta != null;
        sMeta.setDisplayName(ChatHelper.color(title));
        sMeta.setLore(Arrays.stream(lore)
            .map(ChatHelper::color)
            .collect(Collectors.toList()));
        sMeta.setOwner(UUIDFetcher.getName(uuid));
        item.setItemMeta(sMeta);
        return item;
    }

    public static ItemStack createItem(String title, String skullURL, String... lore) {
        ItemStack item = createItem(ChatHelper.color(title), Material.SKULL_ITEM, (short)3, lore);
        return setURL(item, skullURL);
    }

    public static ItemStack createItem(String title, ItemStack i, String... lore) {
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatHelper.color(title));
        meta.setLore(Arrays.asList(lore.clone()));
        i.setItemMeta(meta);
        return i;
    }

    public static String[] createLore(String key, Language lang, String... args) {
        YamlConfiguration langConfig = YMLHelper.getLangFile(lang);
        if (!langConfig.contains(key)) {
            return new ArrayList<String>(){{ add(""); }}.toArray(new String[0]);
        }
        List<String> lines = langConfig.getList(key).stream().map(Object::toString).collect(Collectors.toList());
        List<String> formattedLines = new ArrayList<>();
        for (String line : lines) {
            String str = new MessageFormat(line).format(args);
            formattedLines.add(str);
        }
        return formattedLines.toArray(new String[0]);
    }

    public static String[] createLore(String key, ScovillePlayer sp, String... args) {
        return createLore(key, sp.getLang(), args);
    }

    public static String[] createLore(String key, Player p, String... args) {
        return createLore(key, ScovillePlayer.getPlayer(p), args);
    }

    public static String[] createLore(String... args) {
        return Arrays.stream(args).map(ChatHelper::color).toArray(String[]::new);
    }

    public static String getURL(ItemStack i) {
        if (i.getType() != Material.SKULL_ITEM || i.getData().getData() != 3) return null;
        SkullMeta iMeta = (SkullMeta)i.getItemMeta();
        Field profileField = null;
        try {
            profileField = iMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (profileField == null) return null;
        profileField.setAccessible(true);
        GameProfile profile = null;
        try {
            profile = (GameProfile)profileField.get(iMeta);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (profile == null) return null;
        if (!profile.getProperties().containsKey("textures")) return null;
        Iterator<Property> properties = profile.getProperties().get("textures").iterator();
        Property textureProperty = properties.next();
        String textureString = new String(Base64.decodeBase64(textureProperty.getValue()));
        Pattern urlPattern = Pattern.compile("\"url\":\"(.+)\"");
        Matcher m = urlPattern.matcher(textureString);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    public static ItemStack setURL(ItemStack item, String skullURL) {
        return SkullCreator.itemWithUrl(item, skullURL);
    }

}
