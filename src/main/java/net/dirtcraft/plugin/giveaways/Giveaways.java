package net.dirtcraft.plugin.giveaways;

import com.google.inject.Inject;
import net.dirtcraft.plugin.giveaways.Commands.JoinGiveaway;
import net.dirtcraft.plugin.giveaways.Commands.StartGiveaway;
import net.dirtcraft.plugin.giveaways.Configuration.ConfigManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.Collection;

@Plugin(
        id = "giveaways",
        name = "Giveaways",
        description = "Simple plugin to start giveaways for the whole server!",
        url = "https://dirtcraft.net",
        authors = {
                "juliann"
        }
)
public class Giveaways {

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private ConfigManager cfgManager;

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    public Giveaways instance;
    public ArrayList<String> list = new ArrayList<>();
    public boolean giveawayRunning = false;
    public int giveawaySeconds;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        instance = this;
        loadConfig();
        logger.info("Successfully loaded configuration for " + container.getName() + ".");
    }

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        registerCommands();
        logger.info("Commands for " + container.getName() + " have successfully been registered!");
    }

    private void registerCommands() {
        CommandSpec join = CommandSpec.builder()
                .description(format("Join the giveaway"))
                .permission(Permissions.JOIN)
                .executor(new JoinGiveaway(this))
                .build();

        CommandSpec start = CommandSpec.builder()
                .description(format("Starts the giveaway"))
                .permission(Permissions.START)
                .arguments(
                        GenericArguments.integer(format("seconds")),
                        GenericArguments.remainingJoinedStrings(format("prize"))
                )
                .executor(new StartGiveaway(this))
                .build();

        CommandSpec main = CommandSpec.builder()
                .description(format("Main command for " + container.getName()))
                .child(start, "start")
                .child(join, "join")
                .build();

        Sponge.getCommandManager().register(this, main, "giveaway", "giveaways", "ga");
    }

    private void loadConfig() {
        this.cfgManager = new ConfigManager(loader);
    }

    public PluginContainer getContainer() {
        return container;
    }

    public Collection<Player> getOnlinePlayers() {
        return Sponge.getServer().getOnlinePlayers();
    }

    public Text format(String message) {
        return TextSerializers.FORMATTING_CODE.deserialize(message);
    }

}
