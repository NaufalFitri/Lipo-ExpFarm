package dev.lipoteam.treeCapitator;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands {

    private Configurations config;

    public Commands(Configurations configurations) {

        this.config = configurations;
        TreeCapitator plugin = TreeCapitator.getInstance();
        ConsoleCommandSender console = plugin.getServer().getConsoleSender();

        new CommandAPICommand("treecapitator")
                .withAliases("tc")
                .withPermission("treecapitator.commands")
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission("treecapitator.commands.reload")
                        .executes((sender, args) -> {

                            plugin.ReloadConfig();

                            if (sender instanceof Player) {
                                sender.sendMessage(config.prefix("Reloaded"));
                            } else {
                                console.sendMessage(config.prefix("Reloaded"));
                            }
                        }))
                .register();
    }

    public void setConfig(Configurations config) {
        this.config = config;
    }

}
