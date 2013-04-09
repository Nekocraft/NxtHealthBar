package com.nxtinc.thetestgame.healthbar.listener;

import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.nxtinc.thetestgame.healthbar.PluginMain;

public class DeathListener implements Listener{

	private PluginMain plugin;
	
	public DeathListener(PluginMain plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		try
		{
			String deathMessage = event.getDeathMessage();
			String victim = event.getEntity().getName();
			EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
			if ((damageEvent instanceof EntityDamageByEntityEvent)) {
				Entity damager = ((EntityDamageByEntityEvent)damageEvent).getDamager();
				String[] deathwords = deathMessage.split(" ");
				if (damager != null)
				{
					String deathmsg = victim;
					deathmsg = deathmsg + " " + deathwords[1] + " " + deathwords[2] + " " + deathwords[3];
					if ((damager instanceof Player)) {
						deathwords[4] = ((Player)damager).getName();
						deathmsg = deathmsg + " " + deathwords[4];
						if (deathwords.length > 5)
						{
							for (int i = 5;i <= (deathwords.length - 5); i++)
							{
								deathmsg = deathmsg + "" + deathwords[i];
							}
						}
					}
					if ((damager instanceof LivingEntity)) {
						String temp = WordUtils.capitalizeFully(damager.getType().toString());
						plugin.info(temp);
						deathmsg = deathmsg + " " + temp;
					}
					
					if (deathmsg != null)
					{
						event.setDeathMessage(deathmsg);
						return;
					}
				}
				else
				{
					deathwords[1] = victim;
					String deathmsg = null;
					for (int i = 0; i+1<=deathwords.length; i++)
					{
						deathmsg = deathmsg + deathwords[i];
					}
					
					event.setDeathMessage(deathmsg);
					return;
				}
			}
	    }
	    catch (Exception e) {
	      event.setDeathMessage(event.getEntity().getName() + " died");
	    }
	  }
}
