package dev.lipoteam.expFarm;

import dev.jorel.commandapi.BukkitTooltip;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.Tooltip;
import dev.jorel.commandapi.arguments.EntityTypeArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.SafeSuggestions;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Commands {

    private Configurations config;

    public Commands(Configurations configurations) {

        ExpFarm plugin = ExpFarm.getInstance();
        setConfig(configurations);
        ConsoleCommandSender console = plugin.getServer().getConsoleSender();

        new CommandAPICommand("expfarm")
                .withAliases("xpfarm")
                .withPermission("expfarm.commands")
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission("expfarm.commands.reload")
                        .executes((sender, args) -> {

                            plugin.ReloadConfig();

                            if (sender instanceof Player) {
                                sender.sendMessage(config.prefix("Reloaded"));
                            } else {
                                console.sendMessage(config.prefix("Reloaded"));
                            }
                        }))
                .withSubcommand(new CommandAPICommand("spawner")
                        .withPermission("expfarm.commands.spawner")
                        .withSubcommand(new CommandAPICommand("new")
                                .withArguments(new EntityTypeArgument("entity").replaceSafeSuggestions(SafeSuggestions.suggest(
                                        info -> config.EntityTypes().toArray(new EntityType[0])
                                )))
                                .executesPlayer((sender, args) -> {
                                    plugin.getMethod().AddSpawner(sender, (EntityType) args.get("entity"));
                                }))
                        .withSubcommand(new CommandAPICommand("remove")
                                .withArguments(new IntegerArgument("id").replaceSafeSuggestions(SafeSuggestions.suggest(
                                        info -> config.Ids().keySet().toArray(new Integer[0])
                                )))
                                .executesPlayer((sender, args) -> {
                                    if (config.Ids().keySet().contains((Integer) args.get("id")))
                                        plugin.getMethod().RemoveSpawner((Integer) args.get("id"));
                                })))
                .register();
    }

    public void setConfig(Configurations config) {
        this.config = config;
    }

}
