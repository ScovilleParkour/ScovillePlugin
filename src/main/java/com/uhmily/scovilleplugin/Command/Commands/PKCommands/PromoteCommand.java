package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Plate.Plate;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.Rank;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class PromoteCommand extends ChildCommand {

    public PromoteCommand() {
        PKCommand.getInstance().registerChild("promote", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!p.hasPermission("scoville.pk.promote")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length == 0) {
            return false;
        }

        UUID uuid = UUIDFetcher.getUUID(args[0]);
        ScovillePlayer sp = ScovillePlayer.getPlayer(uuid);
        if (sp == null) return false;

        Rank rank = sp.getRank();
        sp.setRank(rank.nextRank() == null ? Rank.CAPSAICIN : rank.nextRank());
        sp.save();
        if (rank.nextRank() == null) return false;
        sp.getRank().applyRank(uuid);

        p.sendMessage(ChatHelper.format("promoted_player", p, args[0], sp.getRank().getRankColor() + "&l" + sp.getRank().toString()));

        return false;
    }

}
