package dev.lipoteam.expFarm;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EntityEquipment;

import java.util.*;

public class Events implements Listener {

    private List<String> worlds;
    private List<EntityType> mobs;
    private int percentage;
    private int firstrange;
    private int lastrange;
    private int fusemultiplier;
    private int explosion;

    private boolean nodrops;
    private boolean notools;
    private boolean nodamage;
    private boolean multiplier;

    public Events(Configurations config) {
        setConfig(config);
    }

    public void setConfig(Configurations config) {

        this.worlds = config.worlds();
        this.mobs = config.EntityTypes();
        this.multiplier = config.ExpMultiplier();
        this.percentage = config.ExpMultiplierPercentage();
        this.firstrange = config.FirstMultiplierRange();
        this.lastrange = config.LastMultiplierRange();
        this.nodrops = config.NoDrops();
        this.notools = config.NoTools();
        this.nodamage = config.DamageTaken();
        this.fusemultiplier = config.FuseMultiplier();
        this.explosion = config.ExplosionRadius();

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnEntitySpawn(EntitySpawnEvent e) {

        if (e.isCancelled()) return;

        EntityType type = e.getEntityType();

        World world = e.getLocation().getWorld();

        if (world != null) {
            if (!worlds.contains(world.getName())) {
                return;
            }
        }

        if (!(e.getEntity() instanceof LivingEntity entity)) {
            return;
        }

        if (mobs.contains(type)) {

            EntityEquipment equipment = entity.getEquipment();
            if (equipment != null && notools) {
                equipment.clear();
            }

            if (type == EntityType.CREEPER) {

                Creeper creeper = (Creeper) entity;
                creeper.setMaxFuseTicks(creeper.getMaxFuseTicks() * fusemultiplier);
                creeper.setFuseTicks(creeper.getFuseTicks() * fusemultiplier);
                creeper.setExplosionRadius(explosion);

            }
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnEntityDeath(EntityDeathEvent e) {

        EntityType type = e.getEntityType();

        World world = e.getEntity().getLocation().getWorld();

        if (world != null) {
            if (!worlds.contains(world.getName())) {
                return;
            }
        }

        if (nodrops)
            e.getDrops().clear();

        if (mobs.contains(type)) {

            if (multiplier) {

                int ranp = new Random().nextInt(100);
                int ranm = new Random().nextInt(firstrange, lastrange);

                if (e.getDamageSource().getCausingEntity() != null) {
                    if (ranp < percentage && e.getDamageSource().getCausingEntity().getType() == EntityType.PLAYER) {

                        e.setDroppedExp(e.getDroppedExp() * ranm);
                        Player player = (Player) e.getDamageSource().getCausingEntity();
                        String text = "&9Exp &fmultiplied by &9" + ranm + "x";
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(ChatColor.translateAlternateColorCodes('&', text)));

                    }
                }
            }

        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnEntityDamage(EntityDamageEvent e) {

        EntityType type = e.getEntityType();

        World world = e.getEntity().getLocation().getWorld();

        if (world != null) {
            if (!worlds.contains(world.getName())) {
                return;
            }
        }

        if (!nodamage) {
            return;
        }

        if (type == EntityType.PLAYER) {
            e.setCancelled(true);
        }


    }


}
