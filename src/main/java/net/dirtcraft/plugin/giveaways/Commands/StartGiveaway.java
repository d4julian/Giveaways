package net.dirtcraft.plugin.giveaways.Commands;

import net.dirtcraft.plugin.giveaways.Configuration.PluginConfiguration;
import net.dirtcraft.plugin.giveaways.Giveaways;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StartGiveaway implements CommandExecutor {

    private Giveaways main;


    public StartGiveaway(Giveaways main) {
        this.main = main;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (!main.giveawayRunning) {

            int seconds = args.<Integer>getOne("seconds").get();
            String message = args.<String>getOne("prize").get();

            main.giveawaySeconds = seconds;

            if (source instanceof Player) {
                Player player = (Player) source;

                for (MessageReceiver receiver : main.getBroadcast().getMembers()) {
                    giveawayStart(receiver, PluginConfiguration.Messages.startedGiveaway
                            .replace("{playerName}", player.getName())
                            .replace("{giveaway}", message));
                }

                runGiveaway(seconds, message, player);

                return CommandResult.success();

            } else {
                throw new CommandException(main.format("&cOnly a player can start a giveaway!"));
            }
        } else {
            throw new CommandException(main.format("&cA giveaway is already running!"));
        }
    }

    private void runGiveaway(int seconds, String giveaway, Player player) {
        main.giveawayRunning = true;
        Task runScoreboard = Task.builder()
                .name("Running scoreboard")
                .interval(250, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    Scoreboard board = Scoreboard.builder().build();

                    Objective obj = Objective.builder().name(main.getContainer().getId()).criterion(Criteria.DUMMY).displayName(main.format(PluginConfiguration.ScoreBoard.title.replace("{pluginName}", main.getContainer().getName()))).build();

                    board.addObjective(obj);
                    board.updateDisplaySlot(obj, DisplaySlots.SIDEBAR);

                    obj.getOrCreateScore(main.format(PluginConfiguration.ScoreBoard.timeRemaining)).setScore(main.giveawaySeconds);
                    obj.getOrCreateScore(main.format(PluginConfiguration.ScoreBoard.participants)).setScore(main.list.size());

                    for (Player p : Sponge.getServer().getOnlinePlayers()) {
                        p.setScoreboard(board);
                    }
                })
                .submit(main.instance);
        Task count = Task.builder()
                .name("Counting down seconds")
                .async()
                .execute(() -> {
                    main.giveawaySeconds--;
                })
                .interval(1, TimeUnit.SECONDS)
                .submit(main.instance);
        Task.builder()
                .name("Running a giveaway")
                .delay(seconds, TimeUnit.SECONDS)
                .execute(() -> {
                    for (Player receiver : Sponge.getServer().getOnlinePlayers()) {
                        if (main.list.size() == 0) {
                            receiver.sendMessage(main.format(PluginConfiguration.Messages.noParticipants
                            .replace("{playerName}", player.getName())));
                        } else {
                            Random r = new Random();
                            onGiveaway(receiver, PluginConfiguration.Messages.completedGiveaway
                                    .replace("{winner}", main.list.get(r.nextInt(main.list.size())))
                                    .replace("{giveaway}", giveaway));
                        }
                    }
                    main.giveawayRunning = false;
                    main.giveawaySeconds = 0;
                    main.list.clear();
                    count.cancel();
                    runScoreboard.cancel();
                    clearScoreboard();

                })
                .submit(main.instance);
    }

    private PaginationList giveawayStart(MessageReceiver receiver, String giveaway) {
        ArrayList<Text> contents = new ArrayList<>();
        if (PluginConfiguration.Pagination.addSpace) {
            contents.add(main.format("\n" + giveaway + "\n"));
        } else {
            contents.add(main.format(giveaway));
        }

        return
                PaginationList.builder()
                .title(main.format(PluginConfiguration.Pagination.title.replace("{pluginName}", main.getContainer().getName())))
                .padding(main.format(PluginConfiguration.Pagination.padding))
                .contents(contents)
                .footer(
                        Text.builder()
                                .append(main.format(PluginConfiguration.Pagination.footer))
                                .onHover(TextActions.showText(main.format("&6/giveaway join")))
                                .onClick(TextActions.executeCallback(cmd -> {
                                    Player user = (Player) cmd;
                                    if (main.giveawayRunning) {
                                        if (!main.list.contains(user.getName())) {
                                            cmd.sendMessage(main.format(PluginConfiguration.Messages.joinGiveaway
                                                    .replace("{position}", Integer.toString(main.list.size() + 1))
                                                    .replace("{playerName}", cmd.getName())));
                                            main.list.add(cmd.getName());
                                        } else {
                                            cmd.sendMessage(main.format("&cYou have already joined the giveaway!"));
                                        }
                                    } else {
                                        cmd.sendMessage(main.format("&cThe giveaway has already ended!"));
                                    }
                                }))
                                .build()
                )
                        .sendTo(receiver);
    }

    private PaginationList onGiveaway(MessageReceiver receiver, String giveaway) {

        ArrayList<Text> contents = new ArrayList<>();
        if (PluginConfiguration.Pagination.addSpace) {
            contents.add(main.format("\n" + giveaway + "\n"));
        } else {
            contents.add(main.format(giveaway));
        }

        return
                PaginationList.builder()
                        .title(main.format(PluginConfiguration.Pagination.title.replace("{pluginName}", main.getContainer().getName())))
                        .padding(main.format(PluginConfiguration.Pagination.padding))
                        .contents(contents)
                        .sendTo(receiver);
    }

    private void clearScoreboard() {
        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            p.getScoreboard().clearSlot(DisplaySlots.SIDEBAR);
        }
    }
}
