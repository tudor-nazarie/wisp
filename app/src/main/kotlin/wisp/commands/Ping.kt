package wisp.commands

val ping = command {
    name = "ping"
    aliases { +"beep" }
    description = "Replies with pong!"
    handler { message, _, _, _ ->
        val channel = message.channel
        val beeps = listOf(
            "Laughing Beeps",
            "Thankful Beeps",
            "Denying Beeps",
            "Friendly Beeps",
            "Triumphant Beeps",
            "Sorrowful Beeps",
            "Angry Beeps",
            "Meditative Beeps",
            "Ominous Beeps",
        )
        channel.sendMessage(beeps.random()).queue()
    }
}
