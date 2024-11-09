package dev.lipoteam.expFarm;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExpFarm extends JavaPlugin {

    private final ConsoleCommandSender console = getServer().getConsoleSender();
    private Configurations config;

    private Commands commands;
    private Events event;
    private Method method;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true).silentLogs(true));
    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        config = new Configurations(getConfig(), this);
        console.sendMessage(config.prefix("Enabled"));
        method = new Method(config, this);

        RegisterCommands();
        RegisterEvents();

    }

    @Override
    public void onDisable() {
        console.sendMessage(config.prefix("Disabled"));
    }

    private void RegisterEvents() {
        event = new Events(config, this);
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
        Configurations newConfig = new Configurations(config, this);

        commands.setConfig(newConfig);
        method.setConfig(newConfig);
    }

    public Method getMethod() {
        return method;
    }

}
