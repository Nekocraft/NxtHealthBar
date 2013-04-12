package com.nxtinc.thetestgame.healthbar.listener;

import com.nxtinc.thetestgame.healthbar.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class EntityListener implements Listener {

    private final PluginMain plugin;

    public EntityListener(final PluginMain instance) {
        this.plugin = instance;
    }

    public String getColor(final int health) {
        if (health <= 3) {
            return "§c";
        } else if (health <= 10) {
            return "§e";
        } else {
            return "§a";
        }
    }

    private String getDisplayString(final int health, final int max) {
        //This should be a StringBuilder for a better performance
        String temp = getColor(health) + "";
        if (plugin.getConfig().getInt("style", 1) == 3) {
            temp = health + "" + ChatColor.BLACK + "/" + ChatColor.GREEN + max;
        } else {
            for (int i = 1; i <= health; i++) {
                if (plugin.getConfig().getInt("style", 1) == 1) {
                    temp = temp + "▌";
                } else if (plugin.getConfig().getInt("style", 1) == 2) {
                    temp = temp + "|";
                }
            }
        }
        return temp;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMobSpawn(final CreatureSpawnEvent event) {
        final Entity entity = event.getEntity();
        if (plugin.isValidMob(entity)) {
            final LivingEntity mob = (LivingEntity) entity;
            displayMobHealth(mob);
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        displayPlayerHealth(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageEvent(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if ((entity instanceof LivingEntity)) {

            if (entity instanceof Player) {
                displayPlayerHealth((Player) entity);

            } else if (plugin.isValidMob(entity)) {
                final LivingEntity mob = (LivingEntity) entity;
                final String mobName = mob.getCustomName();

                if ((mobName == null) || (mobName.contains("█")) || (mobName.contains("▌")) || (mobName.contains("||")) || (mobName.contains("|")) || (mobName.contains("/"))) {
                    displayMobHealth(mob);
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityRegain(final EntityRegainHealthEvent event) {
        final Entity entity = event.getEntity();

        if ((entity instanceof LivingEntity) && (!(entity instanceof Player)) && (plugin.isValidMob(entity))) {
            final LivingEntity mob = (LivingEntity) entity;
            final String mobName = mob.getCustomName();

            if ((mobName == null) || (mobName.contains("█")) || (mobName.contains("▌")) || (mobName.contains("||")) || (mobName.contains("|")) || (mobName.contains("/"))) {
                displayMobHealth(mob);
                return;
            }
        }
    }

    private void displayPlayerHealth(final Player player) {
        final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        if (plugin.getConfig().getBoolean("playerhealth", false)) {
            board.registerNewObjective("showhealth", "health");
            final Objective objective = board.getObjective("showhealth");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName("/ 20");
        }

        if (plugin.getConfig().getBoolean("healthinterface", false)) {
            board.registerNewObjective("healthui", "health");
            final Objective objective = board.getObjective("healthui");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName("Health");
            final Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Health:"));
            //score.setScore(player.getHealth());
        }

        player.setScoreboard(board);
    }

    private void displayMobHealth(final LivingEntity mob) {
        if (!plugin.getConfig().getBoolean("mobhealth", false)) {
            return;
        }

        final int health = mob.getHealth();

        if (health == 0) {
            mob.setCustomName("");
            mob.setCustomNameVisible(false);
            return;
        }

        mob.setCustomName(getDisplayString(health, mob.getMaxHealth()));
        mob.setCustomNameVisible(!(plugin.getConfig().getBoolean("hide")));
    }
}
