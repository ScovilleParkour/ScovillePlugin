package com.uhmily.scovilleplugin.Command.Commands.TagCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.TagCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Tag.TagManager;
import com.uhmily.scovilleplugin.Types.Tag.TagType;
import me.clip.deluxetags.DeluxeTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand extends ChildCommand {

    public AddCommand() {
        TagCommand.getInstance().registerChild("add", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.tag.add")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length < 2) {
            p.sendMessage(ChatHelper.format("tag_add_help", p));
            return false;
        }

        TagType type = TagType.valueOf(args[0].toUpperCase());
        DeluxeTag tag = DeluxeTag.getLoadedTag(args[1]);

        if (tag == null) {
            return false;
        }

        TagManager.addTag(type, tag);

        p.sendMessage(ChatHelper.format("tag_added", p));

        return false;

    }

}
