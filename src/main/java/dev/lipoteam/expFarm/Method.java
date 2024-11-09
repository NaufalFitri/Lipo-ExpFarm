package dev.lipoteam.expFarm;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Method {

    private Configurations config;
    private ExpFarm plugin;
    private int offset;
    private BukkitScheduler scheduler;

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

        List<String> listspawners = config.ListSpawner();
        listspawners.add(task + " " + world.getName() + " " + x + " " + y + " " + z + " " + type.name());

        int id = config.Ids().size() + 1;

        plugin.getConfig().set("ExpFarm.spawners", listspawners);
        config.Spawners().put(player.getLocation(), type);
        config.TaskList().put(id, task);
        plugin.saveConfig();

    }

    public int SpawnerMethod(Location loc, EntityType type) {
        return scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {

                int ranx = new Random().nextInt(-2,2);
                int ranz = new Random().nextInt(-2,2);
                int nearby = loc.getWorld().getNearbyEntities(loc, 10, 10, 10, entity -> entity instanceof  Player).size();

                if (nearby > 0) {
                    loc.getWorld().spawn(loc.add(ranx, 0, ranz), type.getEntityClass());
                }

            }
        }, 0L, 100L);
    }

    public void RemoveSpawner(int i) {

        DataManager manager = new DataManager(plugin);

        Map<Integer, AbstractMap.SimpleEntry<Location, EntityType>> identity = config.Ids();
        System.out.println(identity);
        Map<Integer, Integer> taskids = config.TaskList();

        int task = taskids.get(i);

        AbstractMap.SimpleEntry<Location, EntityType> spawner = identity.get(i);
        Location loc = spawner.getKey();

        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
            System.out.println(entity.getClass());
            if (entity instanceof TextDisplay) {
                System.out.println(manager.hasData(entity, "spawner"));
                if (manager.hasData(entity, "spawner")) {
                    entity.remove();
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
        config.Ids().remove(i);
        config.Spawners().remove(loc);

    }

}
