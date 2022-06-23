package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DemoteCommand extends ChildCommand {

    public DemoteCommand() {
        PKCommand.getInstance().registerChild("demote", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!p.hasPermission("scoville.pk.demote")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length == 0) {
            return false;
        }

        UUID scovilleUUID = UUIDFetcher.getUUID(args[0]);
        ScovillePlayer sp = ScovillePlayer.getPlayer(scovilleUUID);
        if (sp == null) return false;

        Rank rank = sp.getRank();
        sp.setRank(rank.prevRank() == null ? Rank.BELL : rank.prevRank());
        sp.save();
        if (rank.prevRank() == null) return false;
        sp.getRank().applyRank(scovilleUUID);

        p.sendMessage(ChatHelper.format("demoted_player", p, args[0], sp.getRank().getRankColor() + "&l" + sp.getRank().toString()));

        return false;
    }

}
