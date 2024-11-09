package dev.lipoteam.expFarm;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public class Configurations {

    private final FileConfiguration config;
    private final ExpFarm plugin;

    private Map<Location, EntityType> spawners;
    private final Map<Integer, AbstractMap.SimpleEntry<Location, EntityType>> identifiers = new HashMap<>();
    private final Map<Integer, Integer> taskIds = new HashMap<>();

    public Configurations(FileConfiguration config, ExpFarm plugin) {
        this.plugin = plugin;
        this.config = config;
        LoadSpawner();
    }

    public String prefix(String text) {
        String newtext = config.getString("prefix").replace('&','§') + " " + text.replace('&','§');
        return LegacyComponentSerializer.legacySection().serialize(Component.text(newtext));
    }

    public Boolean isEnabled() {
        return config.getBoolean("ExpFarm.enabled");
    }

    public List<String> worlds() {
        return config.getStringList("ExpFarm.worlds");
    }

    public List<EntityType> EntityTypes() {
        List<String> entities = config.getStringList("ExpFarm.entity-type");
        List<EntityType> types = new ArrayList<>();

        for (String list : entities) {
            types.add(EntityType.valueOf(list));
        }

        return types;
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

    public void LoadSpawner() {

        List<String> spawnerloc = config.getStringList("ExpFarm.spawners");
        System.out.println(spawnerloc);
        Map<Location, EntityType> spawners = new HashMap<>();

        for (String str : spawnerloc) {
            String[] split = str.split(" ");

            int id = Integer.parseInt(split[0]);
            World world = plugin.getServer().getWorld(split[1]);

            double x = Double.parseDouble(split[2]);
            double y = Double.parseDouble(split[3]);
            double z = Double.parseDouble(split[4]);

            Location loc = new Location(world, x, y, z);
            EntityType spawn = EntityType.valueOf(split[5]);

            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {

                    int ranx = new Random().nextInt(-2,2);
                    int ranz = new Random().nextInt(-2,2);
                    int nearby = loc.getWorld().getNearbyEntities(loc, 10, 10, 10, entity -> entity instanceof Player).size();

                    if (nearby > 0) {
                        loc.getWorld().spawn(loc.add(ranx, 0, ranz), spawn.getEntityClass());
                    }

                }
            }, 0L, 100L);;

            spawners.put(loc, spawn);
            taskIds.put(id, task);
            identifiers.put(id, new AbstractMap.SimpleEntry<>(loc, spawn));
        }
        this.spawners = spawners;

    }

    public Map<Location, EntityType> Spawners() {
        return spawners;
    }

    public Map<Integer, AbstractMap.SimpleEntry<Location, EntityType>> Ids() {
        return identifiers;
    }

    public List<String> ListSpawner() {
        return config.getStringList("ExpFarm.spawners");
    }

    public Map<Integer, Integer> TaskList() {
        return taskIds;
    }

}
