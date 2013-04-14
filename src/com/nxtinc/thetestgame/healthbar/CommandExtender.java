package com.nxtinc.thetestgame.healthbar;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExtender implements CommandExecutor {

    private final PluginMain plugin;

    public CommandExtender(final PluginMain instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        // if (cmd.getName().equalsIgnoreCase("nhb")) { It will be every time nhb because this commancexecutor is only registered on that command
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) { //IgnoreCase is more user-friendlier
                if (sender instanceof Player) {
                    final Player player = (Player) sender;
                    if (plugin.hasPermision(player, "nxthealthbar.op")) { //Is Op isn't longer needed because this permission is set as default for op's in the plugin.yml
                        plugin.reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "重新载入完毕!");
                        return true;
                    }
                } else {
                    sender.sendMessage("重新载入中");
                    plugin.reloadConfig();
                    sender.sendMessage("重新载入完毕!");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("version")) {
                if (sender instanceof Player) {
                    final Player player = (Player) sender;
                    if (plugin.hasPermision(player, "nxthealthbar.op")) {
                        sender.sendMessage(String.format("%s版本 : %s", ChatColor.GREEN, plugin.getDescription().getVersion())); //String.format is gives your plugin a higher performance than adding String with +
                        return true;
                    }
                } else {
                    sender.sendMessage(String.format("%版本 : %s", ChatColor.GREEN, plugin.getDescription().getVersion()));
                    return true;
                }
            }
        } else {
            sender.sendMessage(String.format("%s可用子命令", ChatColor.RED));
            sender.sendMessage(String.format("%sreload, version", ChatColor.RED));
        }
        return false;
    }
}
