package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.JsonHelper;
import com.uhmily.scovilleplugin.Types.Music.Song;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadCommand extends ChildCommand {
    public LoadCommand() {
        PKCommand.getInstance().registerChild("load", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.pk.load")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        JsonHelper.loadData();
        Song.loadSongs();

        return false;
    }
}
