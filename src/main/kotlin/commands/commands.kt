package commands

val errorHandler: CommandHandler = { event ->
    val channel = event.channel
    channel.sendMessage("No such command").queue()
}

val ping = Command(
    listOf("ping", "beep"),
    "Replies with pong"
) { event ->
    val channel = event.channel

    val beeps = listOf(
        "Laughing Beeps",
        "Thankful Beeps",
        "Denying Beeps",
        "Friendly Beeps",
        "Triumphant Beeps",
        "Sorrowful Beeps",
        "Angry Beeps",
        "Meditative Beeps",
        "Ominous beeps",
    )

    channel.sendMessage(beeps.random()).queue()
}

val commands: List<Command> = listOf(
    ping,
    heroes,
    players,
    last,
    watch,
)
