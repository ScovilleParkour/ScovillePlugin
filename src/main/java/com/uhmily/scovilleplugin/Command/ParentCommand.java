package com.uhmily.scovilleplugin.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

public abstract class ParentCommand extends BaseCommand {
    public static final HashMap<Class<? extends ParentCommand>, HashMap<String, Class<? extends ChildCommand>>> children = new HashMap<>();

    public ParentCommand(String command) {
        super(command);
    }

    public boolean safeCommand(Player p, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            try {
                return (boolean)this.getClass().getDeclaredMethod("realCommand", Player.class, Command.class, String.class, String[].class).invoke(null, p, cmd, label, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                return false;
            }
        }
        String c = args[0];
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        Class<? extends ChildCommand> command = children.get(getClass()).getOrDefault(c, null);
        if (command != null) {
            try {
                if (ChildCommand.realCommand(p, cmd, label, newArgs))
                    return true;
                command.getDeclaredMethod("realCommand", CommandSender.class, Command.class, String.class, String[].class).invoke(null, p, cmd, label, newArgs);
            } catch (NoSuchMethodException e) {
                p.sendMessage("CLASS DOES NOT HAVE RUNCOMMAND");
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                p.sendMessage("ERROR CALLING RUNCOMMAND");
                e.printStackTrace();
                return true;
            }
        } else {
            try {
                return (boolean)this.getClass().getDeclaredMethod("realCommand", Player.class, Command.class, String.class, String[].class).invoke(null, p, cmd, label, args);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void registerChild(String cmd, ChildCommand child) {
        children.put(getClass(), children.getOrDefault(getClass(), new HashMap<>()));
        children.get(getClass()).put(cmd, child.getClass());
    }

}
