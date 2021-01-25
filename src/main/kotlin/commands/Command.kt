package commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

typealias CommandHandler = suspend (List<String>, MessageReceivedEvent) -> Unit

data class Command(
    val name: String,
    val aliases: List<String> = emptyList(),
    val description: String,
    val subCommands: List<Command> = emptyList(),
    val handler: CommandHandler
)
