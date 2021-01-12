package commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

typealias CommandHandler = suspend (MessageReceivedEvent) -> Unit

data class Command(
    val name: String,
    val description: String,
    val handler: CommandHandler
)
