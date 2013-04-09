package com.nxtinc.thetestgame.healthbar.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.nxtinc.thetestgame.healthbar.PluginMain;

public class EntityListener implements Listener {

	private static PluginMain plugin;
	
	public EntityListener(PluginMain plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) 
	{
		Entity entity = event.getEntity();
		if ((entity instanceof LivingEntity)) 
		{
			if (plugin.isValidMob(entity))
			{
				LivingEntity mob = (LivingEntity)entity;
				displayMobHealth(mob);
			}			
		}
	}
	
	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();	
		displayPlayerHealth(player);
	}

	@EventHandler(ignoreCancelled=true)
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		if ((entity instanceof LivingEntity)) 
		{
			if (entity instanceof Player)
			{
				Player player = (Player) entity;
				displayPlayerHealth(player);
			}
			else
			{
				if (plugin.isValidMob(entity))
				{
					LivingEntity mob = (LivingEntity)entity;
					String mobName = mob.getCustomName();
					if (mobName == null) 
					{
						displayMobHealth(mob);
						return;
					}
					if ((mobName.contains("█")) || (mobName.contains("▌"))|| (mobName.contains("||")) || (mobName.contains("|")) || (mobName.contains("/"))) {
						displayMobHealth(mob);
						return;
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled=true)
	public void onEntityRegain(EntityRegainHealthEvent event)
	{
		Entity entity = event.getEntity();
		if ((entity instanceof LivingEntity)) 
		{
			if (entity instanceof Player)
			{
				
			}
			else
			{
				if (plugin.isValidMob(entity))
				{
					LivingEntity mob = (LivingEntity)entity;
					String mobName = mob.getCustomName();
					if (mobName == null) {
						displayMobHealth(mob);
						return;
					}
					if ((mobName.contains("█")) || (mobName.contains("▌"))|| (mobName.contains("||")) || (mobName.contains("|")) || (mobName.contains("/"))) {
						displayMobHealth(mob);
						return;
					}
				}
			}
		}
	}
	
	public static String getColor(int health)
	{
		String color = null;
		if (health <= 3)
		{
			color = "§c";
		}
		else if (health <= 10)
		{
			color = "§e";
		}
		else if (health <= 20)
		{
			color = "§a";
		}
		return color;
	}

	private static String getDisplayString(int health,int max)
	{
		String temp = getColor(health) + "";
		if (plugin.getConfig().getInt("style",1) == 3)
		{
			temp = health + "" + ChatColor.BLACK + "/"  + ChatColor.GREEN + max;
		}
		else
		{
			for (int i = 1; i <= health; i++)
			{
				if (plugin.getConfig().getInt("style",1) == 1)
				{
					temp = temp + "▌";
				}
				else if (plugin.getConfig().getInt("style",1) == 2)
				{
					temp = temp + "|";
				}
			}
		}
	    return temp;
	}
	
	private void displayPlayerHealth(Player player)
	{
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		board.registerNewObjective("showhealth", "health");
		board.registerNewObjective("healthui", "health");
		if (plugin.getConfig().getBoolean("playerhealth",false))
		{
			Objective objective = board.getObjective("showhealth");
			objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
			objective.setDisplayName("/ 20");
		}
		
		if (plugin.getConfig().getBoolean("healthinterface",false))
		{
			
			Objective objective = board.getObjective("healthui");
		    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		    objective.setDisplayName("Health");
		    Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Health:"));
		    //score.setScore(player.getHealth());
		}
		
		player.setScoreboard(board);
	}

	private void displayMobHealth(final LivingEntity mob)
	{
		if (plugin.getConfig().getBoolean("mobhealth",false))
		{
			final int health = mob.getHealth();
		
			if (health == 0) {
				mob.setCustomName("");
				mob.setCustomNameVisible(false);
				return;
			}
			int maxHealth = mob.getMaxHealth();
			mob.setCustomName(EntityListener.getDisplayString(health,maxHealth));
			mob.setCustomNameVisible(!(plugin.getConfig().getBoolean("hide")));
		}
	}
}
