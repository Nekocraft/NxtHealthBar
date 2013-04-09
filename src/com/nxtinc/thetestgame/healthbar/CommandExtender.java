package com.nxtinc.thetestgame.healthbar;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExtender implements CommandExecutor {

	PluginMain plugin;
	
	public CommandExtender(PluginMain plugin) 
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("nhb"))
		{
			if (args.length > 0)
			{
				if (args[0].equals("reload"))
				{
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						if (plugin.hasPermision(player,"nxthealthbar.op") || player.isOp())
						{
							plugin.Reload();
							sender.sendMessage(ChatColor.GREEN + "Reloaded!");		
							return true;
						}
					}
					else
					{
						sender.sendMessage("Reloading");
						plugin.Reload();
						sender.sendMessage("Reloaded!");
						return true;
					}
				}
				else if (args[0].equals("version"))
				{
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						if (plugin.hasPermision(player,"nxthealthbar.op") || player.isOp())
						{
							sender.sendMessage(ChatColor.GREEN + "Version : " + plugin.getDescription().getVersion());		
							return true;
						}
					}
					else
					{
						sender.sendMessage(ChatColor.GREEN + "Version : " + plugin.getDescription().getVersion());		
						return true;
					}					
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Valid sub commands");
				sender.sendMessage(ChatColor.RED + "reload, version");				
			}
		}
		return false;
	}
}
