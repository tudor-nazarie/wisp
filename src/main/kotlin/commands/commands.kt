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
    channel.sendMessage("Pong!").queue()
}

val commands: List<Command> = listOf(
    ping,
    heroes,
    players,
    last,
)
