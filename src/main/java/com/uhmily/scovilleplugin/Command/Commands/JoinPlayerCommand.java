package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JoinPlayerCommand extends BaseCommand {

    public JoinPlayerCommand() {
        super("joinplayer");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.joinplayer")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (paramArrayOfString.length != 1) return false;

        UUID offlinePlayer = UUIDFetcher.getUUID(paramArrayOfString[0]);
        if (offlinePlayer == null) return false;
        ScovillePlayer sp = ScovillePlayer.getPlayer(offlinePlayer);
        if (sp == null) return false;

        if (!sp.isJoinPlayer()) {
            p.sendMessage(ChatHelper.format("player_joining_disabled", p, paramArrayOfString[0]));
            return false;
        }

        UUID cUUID = sp.getCurrentlyPlaying();
        if (cUUID == null) {
            p.sendMessage(ChatHelper.format("course_not_found", p));
            return false;
        }
        Course c = Course.getCourse(cUUID);
        if (!c.playerHasPermissions(p)) {
            p.sendMessage(ChatHelper.format("course_not_found", p));
            return false;
        }
        c.join(p);
        return false;
    }

}
