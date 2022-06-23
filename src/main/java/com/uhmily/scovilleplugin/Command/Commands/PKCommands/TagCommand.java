package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.TagSign.TagSign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class TagCommand extends ChildCommand {

    public TagCommand() {
        PKCommand.getInstance().registerChild("tag", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!p.hasPermission("scoville.pk.tagsign")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length != 1) {
            p.sendMessage(ChatHelper.format("pk_tagsign_help", p));
            return false;
        }

        Block block = p.getTargetBlock((Set<Material>)null, 5);
        String tagID = args[0];

        if (block == null || (!(block.getState() instanceof Sign) && !(block.getType().equals(Material.IRON_PLATE)))) {
            return false;
        }

        TagSign tagSign = TagSign.getSign(block.getLocation());
        tagSign.setTagID(tagID);
        tagSign.save();

        p.sendMessage(ChatHelper.format("tagsign_created", p));

        return false;
    }
}