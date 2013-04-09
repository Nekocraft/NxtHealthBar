package com.nxtinc.thetestgame.healthbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import com.nxtinc.thetestgame.healthbar.listener.DeathListener;
import com.nxtinc.thetestgame.healthbar.listener.EntityListener;


public class PluginMain extends JavaPlugin{
    private static final Logger log = Logger.getLogger("Minecraft");
    
    public boolean UsingVault = false;
    
    public static Permission permission = null;

    public void info(String text)
    {
    	log.info("[NxtHealthBar] " + text);
    }
    
    public void severe(String text)
    {
    	log.severe("[NxtHealthBar] " + text);    
    }
    
	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		setDefaultConfigs(this.getConfig());
		PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugin("Vault") != null) 
        {
            setupPermissions();
            this.UsingVault = true;
        }
        if (this.getConfig().getBoolean("metrics",true))
        {
	        try 
	        {
	            Metrics metrics = new Metrics(this);
	            info("- Metrics Enabled!");
	            metrics.start();
	        } 
	        catch (IOException e) 
	        {
	        	
	        }
        }
		//* Events
		pm.registerEvents(new EntityListener(this), this);
		pm.registerEvents(new DeathListener(this), this);
		//* Commands
		CommandExtender extender = new CommandExtender(this);
		this.getCommand("nhb").setExecutor(extender);
		info("Enabled");
	}
	
	public void OnDisable()
	{
		info("Disabled");
	}
	
	public void Reload()
	{
		this.reloadConfig();
	}
	
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

	public boolean hasPermision(Player player,String perm)
	{
		if (this.UsingVault)
		{
			if (player.hasPermission(perm) || permission.has(player, perm))
			{
				return true;
			}
			return false;
		}
		else
		{
			return player.hasPermission(perm);
		}
	}
	
	private void setDefaultConfigs(FileConfiguration config)
	{
		if (!config.isSet("hide")) config.set("hide", false);
		if (!config.isSet("style")) config.set("style", 1);
	    if (!config.isSet("metrics")) config.set("metrics", true);
	    
	    List<String> mobs = new ArrayList<String>();
	    mobs.add("ZOMBIE");
	    mobs.add("CREEPER");
	    mobs.add("SKELETON");
	    mobs.add("SPIDER");
	    mobs.add("ENDERMAN");
	    
	    if (!config.isSet("displayfor")) config.set("displayfor", mobs);
	    saveConfig();
	}
	
	public boolean isValidMob(Entity entity)
	{
		List<String> mobs = this.getConfig().getStringList("displayfor");
		if (mobs.size() > 0)
		{
			for (int i = 0; i + 1 <= mobs.size(); i++)
			{
				if (entity.getType().toString().equals(mobs.get(i)))
				{
					return true;
				}
			}
		}
		return false;
	}
}
