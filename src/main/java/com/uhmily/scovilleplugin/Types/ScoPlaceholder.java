package com.uhmily.scovilleplugin.Types;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ScoPlaceholder extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "scoville";
    }

    @Override
    public String getAuthor() {
        return "uhmily";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    public boolean canRegister() {
        return true;
    }

    public boolean persist() {
        return true;
    }

    public String onPlaceholderRequest(Player p, String params) {
        if (p == null) return "Offline";
        if (params.startsWith("author_")) {
            Course c = Course.getCourseOrNull(params.split("author_")[1]);
            if (c == null) return "No Course";
            return UUIDFetcher.getName(c.getAuthorUUID());
        } else if (params.startsWith("cc_")) {
            Course c = Course.getCourseOrNull(params.split("cc_")[1]);
            if (c == null) return "No Course";
            return c.getColoredName();
        } else if (params.startsWith("uuid_")) {
            return UUIDFetcher.getName(UUID.fromString(params.split("uuid_")[1]));
        } else if (params.startsWith("diffname_")) {
            Course c = Course.getCourseOrNull(params.split("diffname_")[1]);
            if (c == null) return "No Course";
            return ChatHelper.format("item_diff_" + c.getDifficulty().toString().toLowerCase(), p);
        } else if (params.startsWith("diffnum_")) {
            Course c = Course.getCourseOrNull(params.split("diffnum_")[1]);
            if (c == null) return "No Course";
            return String.valueOf(c.getDifficulty().toInt());
        } else if (params.startsWith("diffcc_")) {
            Course c = Course.getCourseOrNull(params.split("diffcc_")[1]);
            if (c == null) return "No Course";
            return c.getDifficulty().getColor().toString();
        } else if (params.startsWith("xp")) {
            ScovillePlayer sp = ScovillePlayer.getPlayer(p);
            String out = Long.toString(sp.getXp());
            return out.equals("") ? "0" : out;
        }
        return "";
    }
}
