package dev.lipoteam.expFarm;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.BoundingBox;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Method {

    private Configurations config;
    private final ExpFarm plugin;
    private int offset;
    private final BukkitScheduler scheduler;

    public Method(Configurations config, ExpFarm plugin) {
        this.config = config;
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        setConfig(config);
    }

    public void setConfig(Configurations config) {
        this.config = config;
        this.offset = config.YOffset();
    }

    public void AddSpawner(Player player, EntityType type) {

        DataManager manager = new DataManager(plugin);

        Location loc = player.getLocation().clone();
        loc.setYaw(0);
        loc.setPitch(0);

        TextDisplay label = player.getWorld().spawn(loc.add(0, offset, 0), TextDisplay.class);
        String text = ChatColor.YELLOW + type.name() + " Spawners";
        label.setText(text);
        label.setBillboard(Display.Billboard.CENTER);
        manager.setdata(label, "spawner", true);

        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        World world = player.getWorld();

        int task = SpawnerMethod(loc, type);
        int id = config.ListSpawner().size() + 1;

        List<String> listspawners = config.ListSpawner();
        listspawners.add(id + " " + world.getName() + " " + x + " " + y + " " + z + " " + type.name());

        plugin.getConfig().set("ExpFarm.spawners", listspawners);
        plugin.Spawners().put(player.getLocation(), type);
        plugin.TaskList().put(id, task);
        plugin.saveConfig();

    }

    public int SpawnerMethod(Location loc, EntityType spawn) {
        return scheduler.scheduleSyncRepeatingTask(plugin, () -> {

            int ranx = new Random().nextInt(config.FirstRangeRadius(),config.LastRangeRadius());
            int ranz = new Random().nextInt(config.FirstRangeRadius(),config.LastRangeRadius());

            Location spawnloc = loc.clone();
            World world = loc.getWorld();
            BoundingBox box = BoundingBox.of(loc, config.SpawnRadius(),0,config.SpawnRadius());

            boolean nearby = false;

            Player[] players = new Player[0];

            if (world != null) {
                players = world.getPlayers().toArray(new Player[0]);
            }

            if (config.RadiusType() == 1) {

                for (Player player : players) {
                    if (player.getLocation().distance(loc) < config.SpawnRadius()) {
                        nearby = true;
                    }
                }

                if (config.Particle()) {

                    for (double i = box.getMinX(); i <= box.getMaxX(); i += 0.25) {
                        for (double j = box.getMinZ(); j <= box.getMaxZ(); j += 0.25) {
                            Location newloc = new Location(loc.getWorld(), i, loc.getY(), j);
                            if (world != null) {
                                if (i == box.getMinX() || i == box.getMaxX()) {
                                    world.spawnParticle(config.ParticleType(), newloc, config.ParticleQuantity(), 0, 0, 0, config.ParticleSpeed());
                                }
                                if (j == box.getMinZ() || j == box.getMaxZ()) {
                                    world.spawnParticle(config.ParticleType(), newloc, config.ParticleQuantity(), 0, 0, 0, config.ParticleSpeed());
                                }
                            }

                        }
                    }
                }
            } else {

                for (Player player : players) {
                    if (player.getLocation().distanceSquared(loc) < Math.pow(config.SpawnRadius(), 2)) {
                        nearby = true;
                    }
                }

                if (config.Particle()) {

                    for (double i = 0; i < Math.PI * 2; i += Math.PI * 2 / 100) {
                        double xOffset = config.SpawnRadius() * Math.cos(i);
                        double zOffset = config.SpawnRadius() * Math.sin(i);

                        if (world != null) {
                            Location particleLocation = box.getCenter().toLocation(world).clone().add(xOffset, 0, zOffset);
                            world.spawnParticle(config.ParticleType(), particleLocation, config.ParticleQuantity(), 0, 0, 0, config.ParticleSpeed()); // Adjust particle type and count as needed
                        }
                    }
                }
            }

            if (nearby) {
                if (spawn != null) {
                    assert spawn.getEntityClass() != null;
                    world.spawn(spawnloc.add(ranx, 0, ranz), spawn.getEntityClass());
                }
                world.spawnParticle(Particle.CLOUD, spawnloc, 5, 0,0,0,0.1);
            }

        }, 0L, config.SpawnTime());
    }

    public void RemoveSpawner(int i) {

        DataManager manager = new DataManager(plugin);

        Map<Integer, AbstractMap.SimpleEntry<Location, EntityType>> identity = plugin.Ids();
        System.out.println(identity);
        Map<Integer, Integer> taskids = plugin.TaskList();

        int task = taskids.get(i);

        AbstractMap.SimpleEntry<Location, EntityType> spawner = identity.get(i);
        Location loc = spawner.getKey();
        World world = loc.getWorld();

        if (world != null) {
            for (Entity entity : world.getNearbyEntities(loc, 3, 3, 3)) {
                System.out.println(entity.getClass());
                if (entity instanceof TextDisplay) {
                    System.out.println(manager.hasData(entity, "spawner"));
                    if (manager.hasData(entity, "spawner")) {
                        entity.remove();
                    }

                }
            }
        }

        List<String> listspawners = config.ListSpawner();
        System.out.println(listspawners);
        listspawners.removeIf(list -> {

            String[] split = list.split(" ");
            int id = Integer.parseInt(split[0]);
            return id == i;

        });

        plugin.getConfig().set("ExpFarm.spawners", listspawners);
        plugin.saveConfig();

        scheduler.cancelTask(task);
        plugin.Ids().remove(i);
        plugin.Spawners().remove(loc);

    }

}
