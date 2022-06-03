package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.TPSigns.TPSign;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class TPSignCommand extends ChildCommand {

    public TPSignCommand() {
        PKCommand.getInstance().registerChild("tpsign", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!p.hasPermission("scoville.pk.tpsign")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length < 5) {
            p.sendMessage(ChatHelper.format("pk_tpsign_help", p));
            return false;
        }

        Location tpLoc = new Location(p.getWorld(), Float.parseFloat(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]));

        Block block = p.getTargetBlock((Set<Material>)null, 5);

        if (block == null || !(block.getState() instanceof Sign)) {
            return false;
        }

        TPSign tpSign = TPSign.getSign(block.getLocation());
        tpSign.setTpLoc(tpLoc);
        tpSign.save();

        p.sendMessage(ChatHelper.format("tpsign_created", p));

        return false;
    }
}