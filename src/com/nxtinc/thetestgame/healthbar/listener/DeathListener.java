package com.nxtinc.thetestgame.healthbar.listener;

import com.nxtinc.thetestgame.healthbar.PluginMain;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final PluginMain plugin;

    public DeathListener(final PluginMain instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerDeathEvent(final PlayerDeathEvent event) {
        try {
            final String deathMessage = event.getDeathMessage();
            final String victim = event.getEntity().getName();
            final EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();

            if ((damageEvent instanceof EntityDamageByEntityEvent)) {
                final Entity damager = ((EntityDamageByEntityEvent) damageEvent).getDamager();
                final String[] deathwords = deathMessage.split(" ");

                if (damager == null) {
                    deathwords[1] = victim;
                    String deathmsg = null;
                    for (int i = 0; i + 1 <= deathwords.length; i++) {
                        deathmsg += deathwords[i];
                    }

                    event.setDeathMessage(deathmsg);
                    return;

                } else {
                    String deathmsg = victim;
                    //Here should you use a StringBuilder
                    deathmsg = deathmsg + " " + deathwords[1] + " " + deathwords[2] + " " + deathwords[3];
                    if ((damager instanceof Player)) {
                        deathwords[4] = ((Player) damager).getName();
                        deathmsg = String.format("%s %s", deathmsg, deathwords[4]);
                        if (deathwords.length > 5) {
                            for (int i = 5; i <= (deathwords.length - 5); i++) {
                                deathmsg = deathmsg + "" + deathwords[i];
                            }
                        }
                    }

                    if ((damager instanceof LivingEntity)) {
                        final String temp = WordUtils.capitalizeFully(damager.getType().toString());
                        plugin.info(temp);
                        deathmsg = String.format("%s %s", deathmsg, temp);
                    }

                    if (deathmsg != null) {
                        event.setDeathMessage(deathmsg);
                        return;
                    }

                }
            }
        } catch (Exception e) {
            event.setDeathMessage(String.format("%s died", event.getEntity().getName()));
        }
    }
}
