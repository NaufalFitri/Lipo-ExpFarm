package dev.lipoteam.expFarm;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.*;

public class Configurations {

    private final FileConfiguration config;

    public Configurations(FileConfiguration config) {
        this.config = config;
    }

    public String prefix(String text) {
        String newtext = "";
        if (config.getString("prefix") != null)
            newtext = Objects.requireNonNull(config.getString("prefix")).replace('&','§') + " " + text.replace('&','§');
        return LegacyComponentSerializer.legacySection().serialize(Component.text(newtext));
    }

    public List<String> worlds() {
        return config.getStringList("ExpFarm.worlds");
    }

    public boolean ExpMultiplier() {
        return config.getBoolean("ExpFarm.multiplier.enabled");
    }

    public int ExpMultiplierPercentage() {
        return config.getInt("ExpFarm.multiplier.percentage");
    }

    public int FirstMultiplierRange() {
        return config.getInt("ExpFarm.multiplier.first-range");
    }

    public int LastMultiplierRange() {
        return config.getInt("ExpFarm.multiplier.last-range");
    }

    public int YOffset() {
        return config.getInt("ExpFarm.spawner-options.y-offset");
    }

    public List<EntityType> EntityTypes() {
        List<String> entities = config.getStringList("ExpFarm.entity-type");
        List<EntityType> types = new ArrayList<>();

        for (String list : entities) {
            types.add(EntityType.valueOf(list));
        }

        return types;
    }

    public List<String> ListSpawner() {
        return config.getStringList("ExpFarm.spawners");
    }

    public int FirstRangeRadius() {
        return config.getInt("ExpFarm.spawner-options.random-range-spawn.first");
    }

    public int LastRangeRadius() {
        return config.getInt("ExpFarm.spawner-options.random-range-spawn.last");
    }

    public boolean DamageTaken() {
        return config.getBoolean("ExpFarm.damage-taken");
    }

    public boolean Particle() {
        return config.getBoolean("ExpFarm.spawner-options.particle.enabled");
    }

    public Particle ParticleType() {
        return Particle.valueOf(config.getString("ExpFarm.spawner-options.particle.name"));
    }

    public int ParticleQuantity() {
        return config.getInt("ExpFarm.spawner-options.particle.quantity");
    }

    public double ParticleSpeed() {
        return config.getDouble("ExpFarm.spawner-options.particle.speed");
    }

    public int SpawnRadius() {
        return config.getInt("ExpFarm.spawner-options.radius");
    }

    public int RadiusType() {
        String value = "";

        if (config.getString("prefix") != null)
            value = Objects.requireNonNull(config.getString("ExpFarm.spawner-options.radius-type")).toLowerCase();

        if (value.equals("square")) {
            return 1;
        } else if (value.equals("circle")) {
            return 2;
        }

        return 1;
    }

    public int FuseMultiplier() {
        return config.getInt("ExpFarm.spawner-options.mobs.creeper.fuse-ticks-multiplier");
    }

    public int ExplosionRadius() {
        return config.getInt("ExpFarm.spawner-options.mobs.creeper.explosion");
    }

    public boolean NoTools() {
        return config.getBoolean("ExpFarm.spawner-options.mobs.no-tools");
    }

    public boolean NoDrops() {
        return config.getBoolean("ExpFarm.spawner-options.mobs.no-drops");
    }

    public long SpawnTime() {
        return config.getInt("ExpFarm.spawner-options.spawn-time") * 20L;
    }

    public String roundLocation(Location loc) {
        double x = Math.round(loc.getX() * 100.0) / 100.0;
        double y = Math.round(loc.getY() * 100.0) / 100.0;
        double z = Math.round(loc.getZ() * 100.0) / 100.0;

        return x + " " + y + " " + z;
    }

}
