package dev.lipoteam.treeCapitator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Configurations {

    private final FileConfiguration config;

    public Configurations(FileConfiguration config) {
        this.config = config;
    }

    public String prefix(String text) {
        String newtext = config.getString("prefix").replace('&','§') + " " + text.replace('&','§');
        return LegacyComponentSerializer.legacySection().serialize(Component.text(newtext));
    }

    public Boolean isEnabled() {
        return config.getBoolean("TreeCapitator.enabled");
    }

    public boolean isAsyncMode() {
        return config.getBoolean("TreeCapitator.async");
    }

    public boolean isDelay() {
        return config.getBoolean("TreeCapitator.delay.enabled");
    }

    public boolean BAP() {
        return config.getBoolean("TreeCapitator.delay.BAP");
    }

    public boolean haveParticle() {
        return config.getBoolean("TreeCapitator.particle.enabled");
    }

    public boolean canDamage() {
        return config.getBoolean("TreeCapitator.damage.enabled");
    }

    public boolean needSneak() {
        return config.getBoolean("TreeCapitator.needsneak");
    }

    public boolean isUnbreakingInfluence() {
        return config.getBoolean("TreeCapitator.damage.unbreakingInfluence");
    }

    public boolean isParticleSoundable() {
        return config.getBoolean("TreeCapitator.particle.sound.enabled");
    }

    public Particle particle() {
        return Particle.valueOf(config.getString("TreeCapitator.particle.type"));
    }

    public int particlePercentage() {
        return config.getInt("TreeCapitator.particle.percentage");
    }

    public Sound particleSound() {
        return Sound.valueOf(config.getString("TreeCapitator.particle.sound.name"));
    }

    public int particleAmount() {
        return config.getInt("TreeCapitator.particle.amount");
    }

    public Double particleSpeed() {
        return config.getDouble("TreeCapitator.particle.speed");
    }

    public Long delayTime() {
        return config.getLong("TreeCapitator.delay.time");
    }

    public int extraDamage() {
        return config.getInt("TreeCapitator.damage.extraDamage");
    }

    public List<String> worlds() {
        return config.getStringList("TreeCapitator.worlds");
    }
}
