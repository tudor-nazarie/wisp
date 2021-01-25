package commands

import dsl.command

val ping = command {
    name = "ping"
    aliases { +"beep" }
    description = "Replies with pong!"
    handler { _, event ->
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
}

val commands: List<Command> = listOf(
    ping,
    heroes,
    players,
    last,
    watch,
)
