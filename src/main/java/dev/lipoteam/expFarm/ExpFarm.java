package dev.lipoteam.expFarm;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ExpFarm extends JavaPlugin {

    private final ConsoleCommandSender console = getServer().getConsoleSender();
    private Configurations config;

    private Commands commands;
    private Events event;
    private Method method;

    private Map<Location, EntityType> spawners = new HashMap<>();
    private final Map<Integer, AbstractMap.SimpleEntry<Location, EntityType>> identifiers = new HashMap<>();
    private final Map<Integer, Integer> taskIds = new HashMap<>();

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true).silentLogs(true));
    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        config = new Configurations(getConfig());
        console.sendMessage(config.prefix("Enabled"));
        method = new Method(config, this);

        LoadSpawner();

        RegisterCommands();
        RegisterEvents();

    }

    @Override
    public void onDisable() {
        console.sendMessage(config.prefix("Disabled"));
    }

    private void RegisterEvents() {
        event = new Events(config);
        getServer().getPluginManager().registerEvents(event, this);
    }

    private void RegisterCommands() {
        commands = new Commands(config);
    }

    public static ExpFarm getInstance() {
        return getPlugin(ExpFarm.class);
    }

    public void ReloadConfig() {

        this.reloadConfig();
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        FileConfiguration config = this.getConfig();
        Configurations newConfig = new Configurations(config);

        commands.setConfig(newConfig);
        method.setConfig(newConfig);
        event.setConfig(newConfig);
    }

    public Method getMethod() {
        return method;
    }

    public void LoadSpawner() {

        List<String> spawnerloc = getConfig().getStringList("ExpFarm.spawners");
        Map<Location, EntityType> spawners = new HashMap<>();

        for (String str : spawnerloc) {
            String[] split = str.split(" ");

            int id = Integer.parseInt(split[0]);
            World world = getServer().getWorld(split[1]);

            double x = Double.parseDouble(split[2]);
            double y = Double.parseDouble(split[3]);
            double z = Double.parseDouble(split[4]);

            Location loc = new Location(world, x, y, z);
            EntityType spawn = EntityType.valueOf(split[5]);

            int task = method.SpawnerMethod(loc, spawn);

            spawners.put(loc, spawn);
            taskIds.put(id, task);
            identifiers.put(id, new AbstractMap.SimpleEntry<>(loc, spawn));
        }

        this.spawners = spawners;
    }

    public Map<Location, EntityType> Spawners() {
        return spawners;
    }

    public Map<Integer, Integer> TaskList() {
        return taskIds;
    }

    public Map<Integer, AbstractMap.SimpleEntry<Location, EntityType>> Ids() {
        return identifiers;
    }

}
