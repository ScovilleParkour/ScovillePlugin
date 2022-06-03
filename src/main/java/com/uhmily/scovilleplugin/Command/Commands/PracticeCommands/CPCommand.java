package com.uhmily.scovilleplugin.Command.Commands.PracticeCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PracticeCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CPCommand extends ChildCommand {

    public CPCommand() {
        PracticeCommand.getInstance().registerChild("cp", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!p.hasPermission("scoville.practice.cp")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);

        if (!sp.isPracticing()) return false;
        sp.setPracticeCP(p.getLocation());
        sp.save();

        p.sendMessage(ChatHelper.format("practice_cp_set", p));

        return false;

    }

}
