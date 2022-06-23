package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Memory.TaskManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestartCommand extends ChildCommand {

    public RestartCommand() {
        PKCommand.getInstance().registerChild("restart", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!p.hasPermission("scoville.pk.restart")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        TaskManager.getInstance().getAutoRestartTask().startRestartCountdown();

        return false;
    }

}
