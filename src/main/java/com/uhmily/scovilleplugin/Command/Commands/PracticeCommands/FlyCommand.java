package com.uhmily.scovilleplugin.Command.Commands.PracticeCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PracticeCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand extends ChildCommand {

    public FlyCommand() {
        PracticeCommand.getInstance().registerChild("fly", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!p.hasPermission("scoville.practice.fly")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (!ScovillePlayer.getPlayer(p).isPracticing()) return false;
        p.setAllowFlight(!p.getAllowFlight());

        if (p.getAllowFlight()) {
            p.sendMessage(ChatHelper.format("practice_fly_on", p));
        } else {
            p.sendMessage(ChatHelper.format("practice_fly_off", p));
        }

        return false;

    }

}
