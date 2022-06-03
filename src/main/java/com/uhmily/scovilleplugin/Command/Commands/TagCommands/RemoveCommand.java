package com.uhmily.scovilleplugin.Command.Commands.TagCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.TagCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Tag.TagManager;
import me.clip.deluxetags.DeluxeTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand extends ChildCommand {

    public RemoveCommand() {
        TagCommand.getInstance().registerChild("remove", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.tag.remove")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length < 1) {
            p.sendMessage(ChatHelper.format("tag_remove_help", p));
            return false;
        }

        DeluxeTag tag = DeluxeTag.getLoadedTag(args[0]);

        if (tag == null) {
            return false;
        }

        TagManager.removeTag(tag);

        p.sendMessage(ChatHelper.format("tag_removed", p));

        return false;

    }

}