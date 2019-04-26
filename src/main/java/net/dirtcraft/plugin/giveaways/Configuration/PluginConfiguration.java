package net.dirtcraft.plugin.giveaways.Configuration;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class PluginConfiguration {
    @Setting(value = "Messages")
    private PluginConfiguration.Messages messages = new PluginConfiguration.Messages();
    @Setting(value = "Pagination")
    private PluginConfiguration.Pagination pagination = new PluginConfiguration.Pagination();
    @Setting(value = "Scoreboard")
    private PluginConfiguration.ScoreBoard scoreboard = new PluginConfiguration.ScoreBoard();

    @ConfigSerializable
    public static class Messages {
        @Setting (comment = "Message sent to player when they have joined the giveaway.")
        public static String joinGiveaway = "&aYou have joined the giveaway, &b{playerName}\n&7You are position number &b#{position}";

        @Setting (comment = "Message sent to server when started a giveaway")
        public static String startedGiveaway = "&b{playerName} &7has started a giveaway for &6{giveaway}";

        @Setting (comment = "Message sent to server when giveaway has completed")
        public static String completedGiveaway = "&b{winner} &7has won the giveaway for &6{giveaway}";

        @Setting (comment = "Message sent to server when no one has joined the giveaway")
        public static String noParticipants = "&7No one has joined &b{playerName}&7's giveaway! &c:(";

    }

    @ConfigSerializable
    public static class Pagination {

        @Setting (comment = "Insert Pagination footer here")
        public static String footer = "&8[&aJoin Giveaway&8]";

        @Setting (comment = "Insert Pagination padding here")
        public static String padding = "&7&m-";

        @Setting (comment = "Insert Pagination title here")
        public static String title = "&7&l{pluginName}";

        @Setting (comment = "Adds space in between lines to look nicer!")
        public static boolean addSpace = true;

    }

    @ConfigSerializable
    public static class ScoreBoard {

        @Setting (comment = "Title for top of scoreboard")
        public static String title = "&6{pluginName}";

        @Setting (comment = "Text for time remaining line on scoreboard")
        public static String timeRemaining = "&6Seconds Remaining:";

        @Setting (comment = "Text for participants in the giveaway on the scoreboard")
        public static String participants = "&6Participants:";

    }

}