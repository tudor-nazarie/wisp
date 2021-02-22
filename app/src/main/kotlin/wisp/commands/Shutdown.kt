package wisp.commands

val shutdown = command {
    name = "shutdown"
    description = "Gracefully shuts down the bot"
    restricted = true
    handler { message, _, _, _ ->
        message.channel.sendMessage(
            "Shutting down. Bye bye!"
        ).queue()
        val jda = message.jda
        jda.shutdown()
    }
}