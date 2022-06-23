package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class XPCommand extends ChildCommand {

    public XPCommand() {
        PKCommand.getInstance().registerChild("xp", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!p.hasPermission("scoville.pk.xp.command")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length < 2) {
            p.sendMessage(ChatHelper.format("pk_xp_help", p));
            return false;
        }

        String command = args[0];
        switch (command) {
            case "grant": {
                if (!p.hasPermission("scoville.pk.xp.grant")) {
                    p.sendMessage(ChatHelper.format("no_perms", p));
                    return false;
                }
                if (args.length != 3) {
                    p.sendMessage(ChatHelper.format("pk_xp_grant_help", p));
                    return false;
                }
                UUID uuid = UUIDFetcher.getUUID(args[1]);
                if (uuid == null) {
                    p.sendMessage(ChatHelper.format("unknown_player", p));
                    return false;
                }
                ScovillePlayer sp = ScovillePlayer.getPlayer(uuid);
                if (sp == null) {
                    p.sendMessage(ChatHelper.format("player_never_joined", p));
                    return false;
                }
                long currXp = sp.getXp();
                sp.grantXp(Integer.parseInt(args[2]));
                p.sendMessage(ChatHelper.format("set_xp", p, args[1], currXp, sp.getXp()));
                break;
            }
            case "revoke": {
                if (!p.hasPermission("scoville.pk.xp.revoke")) {
                    p.sendMessage(ChatHelper.format("no_perms", p));
                    return false;
                }
                if (args.length != 3) {
                    p.sendMessage(ChatHelper.format("pk_xp_revoke_help", p));
                    return false;
                }
                UUID uuid = UUIDFetcher.getUUID(args[1]);
                if (uuid == null) {
                    p.sendMessage(ChatHelper.format("unknown_player", p));
                    return false;
                }
                ScovillePlayer sp = ScovillePlayer.getPlayer(uuid);
                if (sp == null) {
                    p.sendMessage(ChatHelper.format("player_never_joined", p));
                    return false;
                }
                long currXp = sp.getXp();
                sp.grantXp(-Integer.parseInt(args[2]));
                p.sendMessage(ChatHelper.format("set_xp", p, args[1], currXp, sp.getXp()));
                break;
            }
            case "set": {
                if (!p.hasPermission("scoville.pk.xp.set")) {
                    p.sendMessage(ChatHelper.format("no_perms", p));
                    return false;
                }
                if (args.length != 3) {
                    p.sendMessage(ChatHelper.format("pk_xp_set_help", p));
                    return false;
                }
                UUID uuid = UUIDFetcher.getUUID(args[1]);
                if (uuid == null) {
                    p.sendMessage(ChatHelper.format("unknown_player", p));
                    return false;
                }
                ScovillePlayer sp = ScovillePlayer.getPlayer(uuid);
                if (sp == null) {
                    p.sendMessage(ChatHelper.format("player_never_joined", p));
                    return false;
                }
                long currXp = sp.getXp();
                sp.setXp(Long.parseLong(args[2]));
                p.sendMessage(ChatHelper.format("set_xp", p, args[1], currXp, sp.getXp()));
                break;
            }
        }

        return false;
    }
}