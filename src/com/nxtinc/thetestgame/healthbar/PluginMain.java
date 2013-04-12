package com.nxtinc.thetestgame.healthbar;

import com.nxtinc.thetestgame.healthbar.listener.DeathListener;
import com.nxtinc.thetestgame.healthbar.listener.EntityListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class PluginMain extends JavaPlugin {

    private static final Logger LOG = Logger.getLogger("Minecraft");
    private Permission permission = null;
    private boolean UsingVault = false;

    public void info(String text) {
        LOG.info(String.format("[NxtHealthBar] %s", text));
    }

    public void severe(String text) {
        LOG.severe(String.format("[NxtHealthBar] %s", text));
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        setDefaultConfigs(this.getConfig());
        final PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugin("Vault") != null) {
            setupPermissions();
            this.UsingVault = true;
        }
        if (this.getConfig().getBoolean("metrics", true)) {
            try {
                final Metrics metrics = new Metrics(this);
                info("- Metrics Enabled!");
                metrics.start();
            } catch (IOException e) {}
        }
        //* Events
        pm.registerEvents(new EntityListener(this), this);
        pm.registerEvents(new DeathListener(this), this);
        //* Commands
        final CommandExtender extender = new CommandExtender(this);
        this.getCommand("nhb").setExecutor(extender);
        info("Enabled");
    }

    @Override
    public void onDisable() {
        info("Disabled");
    }

    public void reload() {
        this.reloadConfig();
    }

    private boolean setupPermissions() {
        final RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);

        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }

        return (permission != null);
    }

    public boolean hasPermision(final Player player, final String perm) {

        if ((this.UsingVault) && (this.permission != null)) {

            if (player.hasPermission(perm) || permission.has(player, perm)) {
                return true;
            }

            return false;
        } else {
            return player.hasPermission(perm);
        }
    }

    private void setDefaultConfigs(final FileConfiguration config) {
        if (!config.isSet("hide")) {
            config.set("hide", false);
        }
        if (!config.isSet("style")) {
            config.set("style", 1);
        }
        if (!config.isSet("metrics")) {
            config.set("metrics", true);
        }
        if (!config.isSet("mobhealth")) {
            config.set("mobhealth", true);
        }
        if (!config.isSet("playerhealth")) {
            config.set("playerhealth", false);
        }
        if (!config.isSet("healthinterface")) {
            config.set("healthinterface", false);
        }

        final List<String> mobs = new ArrayList<String>(5);
        mobs.add("ZOMBIE");
        mobs.add("CREEPER");
        mobs.add("SKELETON");
        mobs.add("SPIDER");
        mobs.add("ENDERMAN");

        if (!config.isSet("displayfor")) {
            config.set("displayfor", mobs);
        }
        saveConfig();
    }

    public boolean isValidMob(final Entity entity) {
        final List<String> mobs = this.getConfig().getStringList("displayfor");
        if (!mobs.isEmpty()) {
            for (int i = 0; i < mobs.size(); i++) {
                if (entity.getType().toString().equals(mobs.get(i))) {
                    return true;
                }
            }
        }

        return false;
    }
}
