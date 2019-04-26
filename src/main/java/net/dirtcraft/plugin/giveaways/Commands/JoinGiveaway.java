package net.dirtcraft.plugin.giveaways.Commands;

import net.dirtcraft.plugin.giveaways.Configuration.PluginConfiguration;
import net.dirtcraft.plugin.giveaways.Giveaways;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

public class JoinGiveaway implements CommandExecutor {

    private Giveaways main;


    public JoinGiveaway(Giveaways main) {
        this.main = main;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (main.giveawayRunning) {
            if (source instanceof Player) {
                Player player = (Player) source;
                if (!main.list.contains(player.getName())) {
                    player.sendMessage(main.format(PluginConfiguration.Messages.joinGiveaway
                            .replace("{position}", Integer.toString(main.list.size() + 1)
                                    .replace("{playerName}", player.getName()))));
                    main.list.add(player.getName());
                } else {
                    throw new CommandException(main.format("&cYou are already in the giveaway!"));
                }
            } else {
                throw new CommandException(main.format("&cYou must be a player to join a giveaway!"));
            }
        } else {
            throw new CommandException(main.format("&cA giveaway is currently not running!"));
        }

        return CommandResult.success();
    }
}
