# Giveaways - Simple plugin to start giveaways for the whole server!
![Image](https://cdn.discordapp.com/attachments/529496121790169119/549811951450980362/b42318a6b4d89a607035038d5d31ba9c.png)

## Support
If you need any help, please contact us on [Discord](https://discord.gg/mGgfyaS)

## Commands/Permissions
**/giveaway start <seconds> <prize...>** - `giveaways.start`
**/giveaway join** - `giveaways.join`

## Configuration
```
Messages {
    # Message sent to server when giveaway has completed
    completedGiveaway="&b{winner} &7has won the giveaway for &6{giveaway}"
    # Message sent to player when they have joined the giveaway.
    joinGiveaway="&aYou have joined the giveaway, &b{playerName}\n&7You are position number &b#{position}"
    # Message sent to server when no one has joined the giveaway
    noParticipants="&7No one has joined &b{playerName}&7's giveaway! &c:("
    # Message sent to server when started a giveaway
    startedGiveaway="&b{playerName} &7has started a giveaway for &6{giveaway}"
}
Pagination {
    # Adds space in between lines to look nicer!
    addSpace=true
    # Insert Pagination footer here
    footer="&8[&aJoin Giveaway&8]"
    # Insert Pagination padding here
    padding="&7&m-"
    # Insert Pagination title here
    title="&6{pluginName}"
}
Scoreboard {
    # Text for participants in the giveaway on the scoreboard
    participants="&6Participants:"
    # Text for time remaining line on scoreboard
    timeRemaining="&6Seconds Remaining:"
    # Title for top of scoreboard
    title="&6{pluginName}"
}

```

## Any issues? Suggestions?
Feel free to contact me on [Discord](https://discord.gg/mGgfyaS) or leave an issue on GitHub.
